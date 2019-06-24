package org.academiadecodigo.org.whiledlings.webserver;

import java.io.*;
import java.net.Socket;


public class RequestHandler implements Runnable {
    public static final String ROOT = "www";
    Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest() throws IOException {
        System.out.println(clientSocket.getRemoteSocketAddress());
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String str = in.readLine();
        String[] s;
        if (str == null || str.isEmpty()) {
            closeClientSocket();
        } else {
            s = str.split(" ");
            if (!s[0].equals("GET")) {
                sendPage("/notallowed.html", Headers.NOTALLOWED);
                return;
            }
            if (s.length > 1) {
                getRequested(s[1]);
            } else {
                DataOutputStream write = new DataOutputStream(clientSocket.getOutputStream());
                write.writeBytes(Headers.BADREQUEST.getHeaderContent());
                closeClientSocket();
            }
        }
    }

    private void getRequested(String str) {
        if (str.equals("/")) {
            sendPage("/index.html", Headers.TEXT);
            return;
        }
        File file = new File(ROOT + str);
        if (!file.exists() || !file.isFile()) {
            sendPage("/404.html", Headers.NOTFOUND);
            return;
        }
        if (str.endsWith(".ico") || str.endsWith(".png") || str.endsWith(".jpg") || str.endsWith(".jar")) {
            sendImage(str);
            return;
        }
        if (str.endsWith(".html")) {
            sendPage(str, Headers.TEXT);
        }
    }

    private void sendPage(String str, Headers header) {
        BufferedOutputStream out = null;
        str = ROOT + str;
        String s;
        s = header.getHeaderContent();
        s = s.replace("<file_byte_size>", new File(str).length() + "");

        try {
            out = new BufferedOutputStream(clientSocket.getOutputStream());
            out.write(s.getBytes());

            BufferedReader bReader = new BufferedReader(new FileReader(str));
            String s1;
            while ((s1 = bReader.readLine()) != null) {
                s1 += "\n";
                out.write(s1.getBytes());
            }
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                closeClientSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendImage(String str) {
        BufferedOutputStream out = null;
        str = ROOT + str;
        String s = Headers.IMAGE.getHeaderContent();
        s = s.replace("<file_byte_size>", new File(str).length() + "");
        String temp[] = str.split("\\.");
        s = s.replace("<image_file_extension>", temp[temp.length - 1]);

        try {
            out = new BufferedOutputStream(clientSocket.getOutputStream());
            out.write(s.getBytes());
            BufferedInputStream bReader = new BufferedInputStream(new FileInputStream(str));
            byte[] b = new byte[1024];
            while ((bReader.read(b, 0, b.length)) != -1) {
                out.write(b);
            }
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                closeClientSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeClientSocket() throws IOException {
        clientSocket.close();
    }
}

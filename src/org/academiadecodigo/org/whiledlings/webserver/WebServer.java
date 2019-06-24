package org.academiadecodigo.org.whiledlings.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WebServer {
    private static int port = 8081;
    private static ServerSocket serverSocket = null;


    public static void main(String[] args) {


        WebServer webServer = new WebServer(8081);
        try {
            serverSocket = new ServerSocket(port);
            ExecutorService cachedPool = Executors.newCachedThreadPool();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                cachedPool.submit(new RequestHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                webServer.closeServerSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private WebServer(int port){
        this.port = port;
    }

    private void closeServerSocket() throws IOException {
        serverSocket.close();
    }
}

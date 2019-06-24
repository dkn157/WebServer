package org.academiadecodigo.org.whiledlings.webserver;

public enum Headers {
    TEXT("HTTP/1.0 200 Document Follows\r\n" + "Content-Type: text/html; charset=UTF-8\r\n" +
            "Content-Length: <file_byte_size>\r\n" + "\r\n"),
    IMAGE("HTTP/1.0 200 Document Follows\r\n" + "Content-Type: image/<image_file_extension> \r\n" +
            "Content-Length: <file_byte_size>\r\n" + "\r\n"),
    NOTFOUND("HTTP/1.0 404 Not Found" + "Content-Type: text/html; charset=UTF-8\r\n" +
            "Content-Length: <file_byte_size>\r\n" + "\r\n"),
    NOTALLOWED("HTTP/1.0 405 Method Not Allowed" + "Content-Type: text/html; charset=UTF-8\r\n" +
            "Content-Length: <file_byte_size>\r\n" + "\r\n"),
    BADREQUEST("HTTP/1.0 400 Bad Request\r\n");

    private String headerContent;

    Headers(String s) {
        headerContent = s;
    }

    public String getHeaderContent() {
        return headerContent;
    }




}

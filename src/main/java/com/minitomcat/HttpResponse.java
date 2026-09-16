package com.minitomcat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private final Socket socket;
    private int statusCode = 200;
    private String body = "";

    public HttpResponse(Socket socket) {
        this.socket = socket;
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // 把响应写回客户端
    public void write() throws IOException {
        String statusLine = (statusCode == 200)
                ? "HTTP/1.1 200 OK\r\n"
                : "HTTP/1.1 404 Not Found\r\n";

        String response = statusLine +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "\r\n" + body;

        OutputStream out = socket.getOutputStream();
        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}

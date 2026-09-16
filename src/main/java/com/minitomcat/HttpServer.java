package com.minitomcat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server is running on port 8080");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("连接进来了！");
            // 处理请求
            HandleRequest.handleRequest(socket); // 处理请求的逻辑已经封装到HandleRequest类中

            // 发送响应
            System.out.println("发送响应");
            // 准备响应体
            String body = "<h1>Hello, MiniTomcat!</h1>";

            // 拼接完整的 HTTP 响应
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Content-Length: " + body.getBytes("UTF-8").length +
                    "\r\n" + "\r\n" + body;

            // 写回给客户端
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(response.getBytes("UTF-8"));
            outputStream.flush();

            // 关闭连接
            socket.close();
            System.out.println("Client disconnected");
        }
    }
}

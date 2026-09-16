package com.minitomcat;

import java.io.*;
import java.net.Socket;

public class HandleRequest {
    public static void handleRequest(Socket socket) throws IOException {
        // 处理请求
        System.out.println("处理请求");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        String request = reader.readLine();
        System.out.println("接收到请求，请求内容为:" + request);
        if(request == null || request.isEmpty()){
            System.out.println("请求为空为空");
            return;
        }
        String address = socket.getInetAddress().toString();
        System.out.println("请求地址: " + address);
        String[] parts = request.split(" ");
        String method = parts[0];  // 请求方法
        String url = parts[1];     // 请求路径
        String version = parts[2]; // 请求版本
        System.out.println("请求方法: " + method);
        System.out.println("请求路径: " + url);
        System.out.println("请求版本: " + version);

        // ===== 路由逻辑 =====
        String body;
        int statusCode;

        if ("/".equals(url)) {
            statusCode = 200;
            body = "<h1>Welcome to MiniTomcat Home Page</h1>";
        } else if ("/hello".equals(url)) {
            statusCode = 200;
            body = "<h1>Hello, MiniTomcat!</h1>";
        } else {
            statusCode = 404;
            body = "<h1>404 Not Found</h1>";
        }

        // ===== 构造响应 =====
        String statusLine;
        if (statusCode == 200) {
            statusLine = "HTTP/1.1 200 OK\r\n";
        } else {
            statusLine = "HTTP/1.1 404 Not Found\r\n";
        }

        String response = statusLine +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + body.getBytes("UTF-8").length + "\r\n" +
                "\r\n" +
                body;

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(response.getBytes("UTF-8"));
        outputStream.flush();

        socket.close();
    }
}

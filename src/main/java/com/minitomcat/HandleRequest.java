package com.minitomcat;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HandleRequest {

    private static final Map<String, String> ROUTES = new HashMap<>();

    static {
        ROUTES.put("/", "<h1>Welcome to MiniTomcat Home Page</h1>");
        ROUTES.put("/hello", "<h1>Hello, MiniTomcat!</h1>");
        ROUTES.put("/time", "<h1>" + new java.util.Date() + "</h1>");
    }

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
        // 简单路由逻辑，根据请求路径返回不同的响应体
        // 从路由表里查
        String body = ROUTES.get(url);
        int statusCode;

        if (body == null) {
            statusCode = 404;
            body = "<h1>404 Not Found</h1>";
        } else {
            statusCode = 200;
        }

        // 构造响应
        String statusLine = (statusCode == 200)
                ? "HTTP/1.1 200 OK\r\n"
                : "HTTP/1.1 404 Not Found\r\n";

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

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
            // 处理客户端请求
            System.out.println("接收到请求，ip地址为:" + socket.getInetAddress());
            // 处理请求
            System.out.println("处理请求");
            // 读取请求行,应该用字符流读取
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            String request = reader.readLine();

            System.out.println("接收到请求，请求内容为:" + request);
            if(request == null || request.isEmpty()){
                System.out.println("请求为空为空");
                continue;
            }

            String[] parts = request.split(" ");
            String method = parts[0];  // GET
            String url = parts[1];     // /hello
            String version = parts[2]; // HTTP/1.1
            System.out.println("请求方法: " + method);
            System.out.println("请求路径: " + url);
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

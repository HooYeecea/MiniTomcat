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
        }
    }
}

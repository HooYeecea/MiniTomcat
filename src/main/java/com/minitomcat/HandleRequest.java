package com.minitomcat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    }
}

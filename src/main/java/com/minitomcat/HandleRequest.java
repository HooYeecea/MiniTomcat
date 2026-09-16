package com.minitomcat;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HandleRequest {

    private static final Map<String, Servlet> SERVLET_MAP = new HashMap<>();

    static {
        SERVLET_MAP.put("/", (req, resp) -> resp.setBody("<h1>Welcome to MiniTomcat Home Page</h1>"));
        SERVLET_MAP.put("/hello", new HelloServlet());
        SERVLET_MAP.put("/time", new TimeServlet());
    }

    public static void handleRequest(Socket socket){

        try (socket) {  // 自动关闭，不管是否抛异常，Socket实现Closeable接口，在try-with-resources会调用close方法关闭连接
            // 处理请求
            System.out.println("处理请求");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            // 1. 读请求行，解析 method / url / version
            String requestLine = reader.readLine();
            System.out.println("接收到请求，请求内容为:" + requestLine);
            if(requestLine == null || requestLine.isEmpty()){
                System.out.println("请求为空为空");
                return;
            }
            String address = socket.getInetAddress().toString();
            System.out.println("请求地址: " + address);
            String[] parts = requestLine.split(" ");
            String method = parts[0];  // 请求方法
            String url = parts[1];     // 请求路径
            String version = parts[2]; // 请求版本
            System.out.println("请求方法: " + method);
            System.out.println("请求路径: " + url);
            System.out.println("请求版本: " + version);

            // ===== 路由逻辑 =====
            // 简单路由逻辑，根据请求路径返回不同的响应体
            // 从路由表里查

            // 2. 封装成 HttpRequest 对象
            HttpRequest request = new HttpRequest();
            request.setMethod(method);
            request.setUrl(url);
            request.setVersion(version);

            // 3. 创建 HttpResponse
            HttpResponse response = new HttpResponse(socket);

            // 4. 从路由表找 Servlet
            Servlet servlet = SERVLET_MAP.get(url);

            if (servlet == null) {
                response.setStatus(404);
                response.setBody("<h1>404 Not Found</h1>");
            } else {
                servlet.service(request, response);
            }

            // 5. 写回响应
            response.write();

        } catch (IOException e) {
            System.err.println("处理请求时发生 IO 异常: " + e.getMessage());
        }
    }
}

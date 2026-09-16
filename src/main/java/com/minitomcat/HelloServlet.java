package com.minitomcat;

public class HelloServlet implements Servlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setBody("<h1>Hello, MiniTomcat!</h1>");
    }
}
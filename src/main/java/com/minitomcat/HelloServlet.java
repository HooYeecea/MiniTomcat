package com.minitomcat;

import com.minispring.web.HttpRequest;
import com.minispring.web.HttpResponse;
import com.minispring.web.Servlet;

public class HelloServlet implements Servlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setBody("<h1>Hello, MiniTomcat!</h1>");
    }
}

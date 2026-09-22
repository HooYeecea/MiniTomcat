package com.minitomcat;

import com.minispring.web.HttpRequest;
import com.minispring.web.HttpResponse;
import com.minispring.web.Servlet;

import java.util.Date;

public class TimeServlet implements Servlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setBody("<h1>" + new Date() + "</h1>");
    }
}

package com.minitomcat;

import java.util.Date;

public class TimeServlet implements Servlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setBody("<h1>" + new Date() + "</h1>");
    }
}

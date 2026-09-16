package com.minitomcat;

import java.io.IOException;

public interface Servlet {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}

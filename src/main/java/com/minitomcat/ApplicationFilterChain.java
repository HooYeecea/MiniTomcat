package com.minitomcat;

import java.io.IOException;
import java.util.List;

/**
 * 模仿 Tomcat 的 ApplicationFilterChain。
 * 每次请求单独创建一份，内部用 pos 记录走到第几个 Filter。
 */
public class ApplicationFilterChain implements FilterChain {
    private final List<Filter> filters;
    private final Servlet servlet;
    private int pos = 0;

    public ApplicationFilterChain(List<Filter> filters, Servlet servlet) {
        this.filters = filters;
        this.servlet = servlet;
    }

    // 递归调用 Filter.doFilter 方法，直到走到最后一个 Filter 或 Servlet。
    @Override
    public void doFilter(HttpRequest request, HttpResponse response) throws IOException {
        if (pos < filters.size()) {
            Filter next = filters.get(pos++);
            next.doFilter(request, response, this);
            return;
        }
        // 走到最后一个 Filter，调用 Servlet.service 方法。
        if (servlet != null) {
            servlet.service(request, response);
        } else {
            response.setStatus(404);
            response.setBody("<h1>404 Not Found</h1>");
        }
    }
}

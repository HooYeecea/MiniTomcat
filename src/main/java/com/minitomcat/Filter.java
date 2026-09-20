package com.minitomcat;

import java.io.IOException;

/**
 * 模仿 Servlet Filter：在 Servlet 前后插入横切逻辑。
 * 想放行请求必须调用 {@code chain.doFilter()}，不调用就等于拦截。
 */
public interface Filter {
    void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws IOException;
}

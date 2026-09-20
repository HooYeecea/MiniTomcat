package com.minitomcat;

import java.io.IOException;

/**
 * 模仿 Servlet FilterChain：按注册顺序调用下一个 Filter，
 * 全部 Filter 走完后再调用目标 Servlet。
 */
public interface FilterChain {
    void doFilter(HttpRequest request, HttpResponse response) throws IOException;
}

package com.minitomcat;

import java.io.IOException;

/**
 * 演示「请求进入 / 离开」日志。映射 {@code /*}，所有请求都会经过。
 */
public class LogFilter implements Filter {
    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws IOException {
        System.out.println("[LogFilter] 进入: " + request.getMethod() + " " + request.getUrl());
        chain.doFilter(request, response);
        System.out.println("[LogFilter] 离开: " + request.getUrl());
    }
}

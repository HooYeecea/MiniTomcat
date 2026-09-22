package com.minitomcat;

import com.web.Filter;
import com.web.FilterChain;
import com.web.Servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleRequest {

    private static final Map<String, Servlet> SERVLET_MAP = new HashMap<>();
    private static final List<FilterMapping> FILTER_MAPPINGS = new ArrayList<>();

    static {
        SERVLET_MAP.put("/", (req, resp) -> resp.setBody("<h1>Welcome to MiniTomcat Home Page</h1>"));
        SERVLET_MAP.put("/hello", new HelloServlet());
        SERVLET_MAP.put("/time", new TimeServlet());

        FILTER_MAPPINGS.add(new FilterMapping("/*", new LogFilter()));
        FILTER_MAPPINGS.add(new FilterMapping("/*", new TimerFilter()));
        FILTER_MAPPINGS.add(new FilterMapping("/hello", new HelloFilter()));
    }

    private static List<Filter> matchFilters(String url) {
        List<Filter> matched = new ArrayList<>();
        for (FilterMapping mapping : FILTER_MAPPINGS) {
            if (mapping.matches(url)) {
                matched.add(mapping.getFilter());
            }
        }
        return matched;
    }

    public static void handleRequest(Socket socket) {

        try (socket) {
            System.out.println("处理请求");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            String requestLine = reader.readLine();
            System.out.println("接收到请求，请求内容为:" + requestLine);
            if (requestLine == null || requestLine.isEmpty()) {
                System.out.println("请求为空为空");
                return;
            }
            String address = socket.getInetAddress().toString();
            System.out.println("请求地址: " + address);
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String url = parts[1];
            String version = parts[2];
            System.out.println("请求方法: " + method);
            System.out.println("请求路径: " + url);
            System.out.println("请求版本: " + version);

            BioHttpRequest request = new BioHttpRequest();
            request.setMethod(method);
            request.setUrl(url);
            request.setVersion(version);

            BioHttpResponse response = new BioHttpResponse(socket);

            Servlet servlet = SERVLET_MAP.get(url);
            FilterChain chain = new ApplicationFilterChain(matchFilters(url), servlet);
            chain.doFilter(request, response);

            response.write();

        } catch (Exception e) {
            System.err.println("处理请求时发生异常: " + e.getMessage());
        }
    }
}

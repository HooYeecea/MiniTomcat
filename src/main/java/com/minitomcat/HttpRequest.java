package com.minitomcat;

public class HttpRequest {
    private String method;
    private String url;
    private String version;

    // 构造方法、getter/setter 自己补全
    public HttpRequest() {
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}

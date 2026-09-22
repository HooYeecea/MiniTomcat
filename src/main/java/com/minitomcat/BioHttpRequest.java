package com.minitomcat;

import com.web.HttpRequest;

/**
 * BIO connector request. Only method / URI / version for now.
 */
public class BioHttpRequest implements HttpRequest {
    private String method;
    private String url;
    private String version;

    public BioHttpRequest() {
    }

    @Override
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    /** @deprecated prefer {@link #getUri()} / {@link #getPath()} */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUri() {
        return url;
    }

    @Override
    public String getPath() {
        if (url == null || url.isEmpty()) {
            return "/";
        }
        int query = url.indexOf('?');
        String path = query >= 0 ? url.substring(0, query) : url;
        return path.isEmpty() ? "/" : path;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

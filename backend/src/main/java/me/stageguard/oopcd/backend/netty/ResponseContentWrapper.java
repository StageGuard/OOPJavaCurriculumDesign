package me.stageguard.oopcd.backend.netty;

import io.netty.handler.codec.http.HttpResponseStatus;

public class ResponseContentWrapper {
    private final HttpResponseStatus status;
    private final String content;

    public ResponseContentWrapper(HttpResponseStatus status, String content) {
        this.status = status;
        this.content = content;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }
}

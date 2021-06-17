package me.stageguard.oopcd.backend.netty;

import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.netty.dto.IResponseDTO;

public class ResponseContentWrapper {
    private final HttpResponseStatus status;
    private final IResponseDTO content;

    public ResponseContentWrapper(HttpResponseStatus status, IResponseDTO content) {
        this.status = status;
        this.content = content;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public String getContent() {
        return content.serialize();
    }
}

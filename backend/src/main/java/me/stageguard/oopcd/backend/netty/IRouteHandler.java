package me.stageguard.oopcd.backend.netty;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

@Route
public interface IRouteHandler {
    ResponseContentWrapper handle(FullHttpRequest request);
}

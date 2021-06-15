package me.stageguard.oopcd.backend.netty.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;

@Route(method = "POST", path = "/nmsl")
public class TestPostHandler implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        return new ResponseContentWrapper(HttpResponseStatus.OK, "TestPost");
    }
}

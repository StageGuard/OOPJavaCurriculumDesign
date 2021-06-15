package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.database.Database;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import me.stageguard.oopcd.backend.netty.dto.request.SqlStatementDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlExecuteResultDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlQueryResultDTO;

import java.nio.charset.StandardCharsets;

@Route(path = "/test/pg", method = RouteType.COMPOUND)
public class TestPostGetRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        return new ResponseContentWrapper(
                HttpResponseStatus.OK,
                "Test" + (request.method().toString().equals(RouteType.POST) ? "Post" : "GET")
        );
    }
}
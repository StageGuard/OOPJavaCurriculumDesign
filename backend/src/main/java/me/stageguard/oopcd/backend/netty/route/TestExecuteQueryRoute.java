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

@Route(path = "/test/executeSQLStatement", method = RouteType.POST)
public class TestExecuteQueryRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var dto = SqlStatementDTO.deserialize(content);
        if(dto.expression.trim().toLowerCase().startsWith("select")) {
            var execute = Database.queryBlocking(dto.expression);
            if(execute.isEmpty()) {
                return new ResponseContentWrapper(HttpResponseStatus.NO_CONTENT, "");
            } else  {
                var result = execute.get();
                return new ResponseContentWrapper(
                    HttpResponseStatus.OK,
                    (new SqlQueryResultDTO(result)).serialize()
                );
            }
        }
        var execute = Database.executeBlocking(dto.expression);
        return new ResponseContentWrapper(
            HttpResponseStatus.OK,
            (new SqlExecuteResultDTO(execute)).serialize()
        );
    }
}

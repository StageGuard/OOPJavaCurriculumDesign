package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import me.stageguard.oopcd.backend.netty.dto.request.ImportSingleStudentDTO;

import java.nio.charset.StandardCharsets;

@Route(path = "v1/importSingleStudent", method = RouteType.POST)
public class ImportSingleStudentRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var studentInfo = ImportSingleStudentDTO.deserialize(content);
        return new ResponseContentWrapper(HttpResponseStatus.OK, "");
    }
}
package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import me.stageguard.oopcd.backend.netty.dto.request.RollRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.RollResponseDTO;
import me.stageguard.oopcd.backend.roll.SessionManager;

import java.nio.charset.StandardCharsets;

@Route(path = "/v1/roll", method = RouteType.POST)
public class RollRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var sessionKey = RollRequestDTO.deserialize(content);
        var session = SessionManager.INSTANCE.getSession(sessionKey.sessionKey);
        if (session != null) {
            try {
                var student = session.roll();
                return new ResponseContentWrapper(HttpResponseStatus.OK, new RollResponseDTO(student));
            } catch (Exception ex) {
                return new ResponseContentWrapper(HttpResponseStatus.OK, new ErrorResponseDTO(
                        "Error occurred while rolling: " + ex
                ));
            }
        } else {
            return new ResponseContentWrapper(HttpResponseStatus.OK, new ErrorResponseDTO(
                    "Session " + sessionKey.sessionKey + " is not exist!"
            ));
        }
    }
}

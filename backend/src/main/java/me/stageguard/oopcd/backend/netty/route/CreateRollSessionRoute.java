package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import me.stageguard.oopcd.backend.netty.dto.request.CreateRollSessionRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.response.CreateRollSessionResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.roll.SessionManager;

import java.nio.charset.StandardCharsets;

@Route(path = "/v1/createRollSession", method = RouteType.POST)
public class CreateRollSessionRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var config = CreateRollSessionRequestDTO.deserialize(content);
        try {
            if (config.config != null) {
                SessionManager.INSTANCE.setDefaultConfig(config.config);
            }
            return new ResponseContentWrapper(
                    HttpResponseStatus.OK,
                    new CreateRollSessionResponseDTO(SessionManager.INSTANCE.createNewSession())
            );
        } catch (Exception ex) {
            return new ResponseContentWrapper(
                    HttpResponseStatus.OK,
                    new ErrorResponseDTO("Unable to create a new roll session: " + ex)
            );
        }
    }
}

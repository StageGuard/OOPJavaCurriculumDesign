package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import me.stageguard.oopcd.backend.netty.dto.request.AnswerRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.response.AnswerResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.roll.SessionManager;

import java.nio.charset.StandardCharsets;

@Route(path = "/v1/answer", method = RouteType.POST)
public class AnswerRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var answerInfo = AnswerRequestDTO.deserialize(content);
        var session = SessionManager.INSTANCE.getSession(answerInfo.sessionKey);
        if (session != null) {
            try {
                if (answerInfo.isRight) {
                    session.answerRight();
                    SessionManager.INSTANCE.deleteSession(answerInfo.sessionKey);
                } else {
                    session.answerWrong();
                }
                return new ResponseContentWrapper(HttpResponseStatus.OK, new AnswerResponseDTO("operation success."));
            } catch (Exception ex) {
                return new ResponseContentWrapper(HttpResponseStatus.OK, new ErrorResponseDTO(
                        "Error occurred while rolling: " + ex
                ));
            }
        } else {
            return new ResponseContentWrapper(HttpResponseStatus.OK, new ErrorResponseDTO(
                    "Session " + answerInfo.sessionKey + " is not exist!"
            ));
        }
    }
}

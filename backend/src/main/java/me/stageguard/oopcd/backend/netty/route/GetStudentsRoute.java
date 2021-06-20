package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.database.dao.StudentDAO;
import me.stageguard.oopcd.backend.netty.*;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;

import java.util.HashMap;

@Route(path = "/v1/getStudents", method = RouteType.COMPOUND)
public class GetStudentsRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        try {
            if (request.method() == HttpMethod.GET) {
                HashMap<String, String> queryOpinions = Utils.decodeQueryOpinions(request.uri());
                var students = StudentDAO.INSTANCE.retrieve(
                        filter -> filter,
                        Long.parseLong(queryOpinions.getOrDefault("limit", "10000"))
                );

            } else if (request.method() == HttpMethod.POST) {

            } else {
                return new ResponseContentWrapper(
                        HttpResponseStatus.NOT_FOUND,
                        new ErrorResponseDTO("Method " + request.method() + " is not allowed.")
                );
            }
        } catch (Exception ex) {
            return new ResponseContentWrapper(
                    HttpResponseStatus.NOT_FOUND,
                    new ErrorResponseDTO("Error while get students: " + ex);
            );
        }
    }
}

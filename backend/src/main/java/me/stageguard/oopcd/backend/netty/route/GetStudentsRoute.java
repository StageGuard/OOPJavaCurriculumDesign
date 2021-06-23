package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.database.ConditionFilter.Condition;
import me.stageguard.oopcd.backend.database.dao.StudentDAO;
import me.stageguard.oopcd.backend.netty.*;
import me.stageguard.oopcd.backend.netty.dto.request.GetStudentsRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.GetStudentsResponseDTO;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
                return new ResponseContentWrapper(
                        HttpResponseStatus.OK,
                        new GetStudentsResponseDTO(students)
                );
            } else if (request.method() == HttpMethod.POST) {
                var content = request.content().toString(StandardCharsets.UTF_8);
                var requestDTO = GetStudentsRequestDTO.deserialize(content);
                var filters = requestDTO.filterOpinions;
                var students = StudentDAO.INSTANCE.retrieve(filter -> {
                    AtomicBoolean head = new AtomicBoolean(false);
                    filters.forEach((op, value) -> {
                        switch (op) {
                            case "equal":
                                if (!head.get()) {
                                    head.set(true);
                                    filter.head(Condition.eq(value.getFirst(), value.getSecond()));
                                } else {
                                    filter.and(Condition.eq(value.getFirst(), value.getSecond()));
                                }
                                break;
                            case "notEqual":
                                if (!head.get()) {
                                    head.set(true);
                                    filter.head(Condition.notEq(value.getFirst(), value.getSecond()));
                                } else {
                                    filter.and(Condition.notEq(value.getFirst(), value.getSecond()));
                                }
                                break;
                            case "less":
                                if (!head.get()) {
                                    head.set(true);
                                    filter.head(Condition.less(value.getFirst(), value.getSecond()));
                                } else {
                                    filter.and(Condition.less(value.getFirst(), value.getSecond()));
                                }
                                break;
                            case "lessEqual":
                                if (!head.get()) {
                                    head.set(true);
                                    filter.head(Condition.lessEq(value.getFirst(), value.getSecond()));
                                } else {
                                    filter.and(Condition.lessEq(value.getFirst(), value.getSecond()));
                                }
                                break;
                            case "greater":
                                if (!head.get()) {
                                    head.set(true);
                                    filter.head(Condition.greater(value.getFirst(), value.getSecond()));
                                } else {
                                    filter.and(Condition.greater(value.getFirst(), value.getSecond()));
                                }
                                break;
                            case "greaterEqual":
                                if (!head.get()) {
                                    head.set(true);
                                    filter.head(Condition.greaterEq(value.getFirst(), value.getSecond()));
                                } else {
                                    filter.and(Condition.greaterEq(value.getFirst(), value.getSecond()));
                                }
                                break;
                            default:
                                break;
                        }
                    });
                    return filter;
                }, requestDTO.limit);
                return new ResponseContentWrapper(
                        HttpResponseStatus.OK,
                        new GetStudentsResponseDTO(students)
                );
            } else {
                return new ResponseContentWrapper(
                        HttpResponseStatus.NOT_FOUND,
                        new ErrorResponseDTO("Method " + request.method() + " is not allowed.")
                );
            }
        } catch (Exception ex) {
            return new ResponseContentWrapper(
                    HttpResponseStatus.NOT_FOUND,
                    new ErrorResponseDTO("Error while get students: " + ex)
            );
        }
    }
}

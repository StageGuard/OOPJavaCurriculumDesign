package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.database.dao.StudentDAO;
import me.stageguard.oopcd.backend.database.dao.StudentDAO.StudentData;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import me.stageguard.oopcd.backend.netty.dto.request.ImportStudentsDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlExecuteResultDTO;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Route(path = "/v1/importStudents", method = RouteType.POST)
public class ImportStudentsRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var stuImport = ImportStudentsDTO.deserialize(content);
        try {
            StudentDAO.INSTANCE.create();
            var mappedList = stuImport.students.stream()
                    .map((e) -> new StudentData(e.name, e.id, e.clazz))
                    .collect(Collectors.toList());
            int sqlResult = StudentDAO.INSTANCE.insert(mappedList);
            return new ResponseContentWrapper(HttpResponseStatus.OK, new SqlExecuteResultDTO(sqlResult));
        } catch (Exception ex) {
            return new ResponseContentWrapper(HttpResponseStatus.OK, new ErrorResponseDTO(ex.toString()));
        }
    }
}
package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.database.dao.StudentDAO;
import me.stageguard.oopcd.backend.database.dao.StudentDAO.StudentData;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import me.stageguard.oopcd.backend.netty.dto.request.ImportSingleStudentDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlExecuteResultDTO;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@Route(path = "v1/importSingleStudent", method = RouteType.POST)
public class ImportSingleStudentRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var stuImport = ImportSingleStudentDTO.deserialize(content);
        try {
            StudentDAO.INSTANCE.create();
            int sqlResult = StudentDAO.INSTANCE.insert(new StudentData(stuImport.name, stuImport.id, stuImport.clazz));
            return new ResponseContentWrapper(HttpResponseStatus.OK, new SqlExecuteResultDTO(sqlResult));
        } catch (SQLException sqlex) {
            return new ResponseContentWrapper(HttpResponseStatus.OK, new ErrorResponseDTO(sqlex.toString()));
        }
    }
}
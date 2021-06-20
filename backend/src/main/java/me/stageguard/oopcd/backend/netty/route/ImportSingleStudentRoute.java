/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.netty.route;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.database.dao.StudentDAO;
import me.stageguard.oopcd.backend.database.dao.StudentDAO.StudentData;
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.ResponseContentWrapper;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import me.stageguard.oopcd.backend.netty.dto.request.ImportSingleStudentRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlExecuteResponseDTO;

import java.nio.charset.StandardCharsets;

@Route(path = "/v1/importSingleStudent", method = RouteType.POST)
public class ImportSingleStudentRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var stuImport = ImportSingleStudentRequestDTO.deserialize(content);
        try {
            StudentDAO.INSTANCE.create();
            int sqlResult = StudentDAO.INSTANCE.insert(new StudentData(stuImport.name, stuImport.id, stuImport.clazz));
            return new ResponseContentWrapper(HttpResponseStatus.OK, new SqlExecuteResponseDTO(sqlResult));
        } catch (Exception ex) {
            return new ResponseContentWrapper(HttpResponseStatus.OK, new ErrorResponseDTO(ex.toString()));
        }
    }
}
/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.netty.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.stageguard.oopcd.backend.netty.dto.request.GetStudentsRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.request.ImportSingleStudentRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.request.ImportStudentsRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.request.SqlStatementRequestDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.GetStudentsResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlExecuteResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlQueryResponseDTO;

public class GlobalGson {
    public static Gson INSTANCE = (new GsonBuilder())
            //deserializers
            .registerTypeAdapter(SqlStatementRequestDTO.class, new SqlStatementRequestDTO.Deserializer())
            .registerTypeAdapter(ImportSingleStudentRequestDTO.class, new ImportSingleStudentRequestDTO.Deserializer())
            .registerTypeAdapter(GetStudentsRequestDTO.class, new GetStudentsRequestDTO.Deserializer())
            .registerTypeAdapter(ImportStudentsRequestDTO.class, new ImportStudentsRequestDTO.Deserializer())
            //serializers
            .registerTypeAdapter(SqlExecuteResponseDTO.class, new SqlExecuteResponseDTO.Serializer())
            .registerTypeAdapter(SqlQueryResponseDTO.class, new SqlQueryResponseDTO.Serializer())
            .registerTypeAdapter(ErrorResponseDTO.class, new ErrorResponseDTO.Serializer())
            .registerTypeAdapter(GetStudentsResponseDTO.class, new GetStudentsResponseDTO.Serializer())
            .create();
}

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
import me.stageguard.oopcd.backend.netty.dto.request.*;
import me.stageguard.oopcd.backend.netty.dto.response.*;

public class GlobalGson {
    public final static Gson INSTANCE = (new GsonBuilder())
            //deserializers
            .registerTypeAdapter(SqlStatementRequestDTO.class, new SqlStatementRequestDTO.Deserializer())
            .registerTypeAdapter(ImportSingleStudentRequestDTO.class, new ImportSingleStudentRequestDTO.Deserializer())
            .registerTypeAdapter(GetStudentsRequestDTO.class, new GetStudentsRequestDTO.Deserializer())
            .registerTypeAdapter(ImportStudentsRequestDTO.class, new ImportStudentsRequestDTO.Deserializer())
            .registerTypeAdapter(CreateRollSessionRequestDTO.class, new CreateRollSessionRequestDTO.Deserializer())
            .registerTypeAdapter(RollRequestDTO.class, new RollRequestDTO.Deserializer())
            .registerTypeAdapter(AnswerRequestDTO.class, new AnswerRequestDTO.Deserializer())
            //serializers
            .registerTypeAdapter(SqlExecuteResponseDTO.class, new SqlExecuteResponseDTO.Serializer())
            .registerTypeAdapter(SqlQueryResponseDTO.class, new SqlQueryResponseDTO.Serializer())
            .registerTypeAdapter(ErrorResponseDTO.class, new ErrorResponseDTO.Serializer())
            .registerTypeAdapter(GetStudentsResponseDTO.class, new GetStudentsResponseDTO.Serializer())
            .registerTypeAdapter(CreateRollSessionResponseDTO.class, new CreateRollSessionResponseDTO.Serializer())
            .registerTypeAdapter(RollResponseDTO.class, new RollResponseDTO.Serializer())
            .registerTypeAdapter(AnswerResponseDTO.class, new AnswerResponseDTO.Serializer())
            .create();
}

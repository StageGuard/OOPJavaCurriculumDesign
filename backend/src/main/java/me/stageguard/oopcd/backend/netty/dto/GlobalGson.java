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
import me.stageguard.oopcd.backend.netty.dto.request.ImportSingleStudentDTO;
import me.stageguard.oopcd.backend.netty.dto.request.SqlStatementDTO;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlExecuteResultDTO;
import me.stageguard.oopcd.backend.netty.dto.response.SqlQueryResultDTO;

public class GlobalGson {
    public static Gson INSTANCE = (new GsonBuilder())
            .registerTypeAdapter(SqlStatementDTO.class, new SqlStatementDTO.Deserializer())
            .registerTypeAdapter(SqlExecuteResultDTO.class, new SqlExecuteResultDTO.Serializer())
            .registerTypeAdapter(SqlQueryResultDTO.class, new SqlQueryResultDTO.Serializer())
            .registerTypeAdapter(ErrorResponseDTO.class, new ErrorResponseDTO.Serializer())
            .registerTypeAdapter(ImportSingleStudentDTO.class, new ImportSingleStudentDTO.Deserializer())
            .create();
}

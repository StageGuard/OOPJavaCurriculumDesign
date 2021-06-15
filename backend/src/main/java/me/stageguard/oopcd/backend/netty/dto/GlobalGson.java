package me.stageguard.oopcd.backend.netty.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    .create();
}

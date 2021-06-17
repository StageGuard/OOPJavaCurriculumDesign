package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.*;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;
import me.stageguard.oopcd.backend.netty.dto.IResponseDTO;

import java.lang.reflect.Type;
import java.sql.ResultSet;

public class SqlQueryResultDTO implements IResponseDTO {
    public ResultSet result;

    public SqlQueryResultDTO(ResultSet resultSet) {
        this.result = resultSet;
    }

    @Override
    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<SqlQueryResultDTO> {

        @Override
        public JsonElement serialize(SqlQueryResultDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("result", src.result.toString());
            return jsonObject;
        }
    }
}
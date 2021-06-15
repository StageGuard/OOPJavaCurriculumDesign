package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.*;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.lang.reflect.Type;

public class SqlExecuteResultDTO {
    public boolean result;

    public SqlExecuteResultDTO(boolean result) {
        this.result = result;
    }

    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<SqlExecuteResultDTO> {
        @Override
        public JsonElement serialize(SqlExecuteResultDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("result", src.result);
            return jsonObject;
        }
    }
}
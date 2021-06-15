package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.mysql.cj.xdevapi.JsonString;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.sql.ResultSet;

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
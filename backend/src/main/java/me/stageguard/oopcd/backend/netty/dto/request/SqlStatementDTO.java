package me.stageguard.oopcd.backend.netty.dto.request;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.io.StringReader;
import java.lang.reflect.Type;

public class SqlStatementDTO {
    public String expression;

    public static SqlStatementDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), SqlStatementDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<SqlStatementDTO> {
        @Override
        public SqlStatementDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            SqlStatementDTO obj = new SqlStatementDTO();
            JsonObject jsonObject = json.getAsJsonObject();
            obj.expression = jsonObject.get("expression").getAsString();
            return obj;
        }
    }
}

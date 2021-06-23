package me.stageguard.oopcd.backend.netty.dto.request;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.io.StringReader;
import java.lang.reflect.Type;

public class AnswerRequestDTO {
    public String sessionKey = null;
    public boolean answerRight;

    public static AnswerRequestDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), AnswerRequestDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<AnswerRequestDTO> {
        @Override
        public AnswerRequestDTO deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            AnswerRequestDTO obj = new AnswerRequestDTO();
            JsonObject jsonObject = json.getAsJsonObject();
            obj.sessionKey = jsonObject.get("sessionKey").getAsString();
            obj.answerRight = jsonObject.get("isRight").getAsBoolean();
            return obj;
        }
    }
}

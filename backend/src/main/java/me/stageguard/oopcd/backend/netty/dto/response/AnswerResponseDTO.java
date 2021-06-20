package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;
import me.stageguard.oopcd.backend.netty.dto.IResponseDTO;

import java.lang.reflect.Type;

public class AnswerResponseDTO implements IResponseDTO {
    public String result;

    public AnswerResponseDTO(String result) {
        this.result = result;
    }

    @Override
    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<AnswerResponseDTO> {
        @Override
        public JsonElement serialize(AnswerResponseDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("result", src.result);
            return jsonObject;
        }
    }
}

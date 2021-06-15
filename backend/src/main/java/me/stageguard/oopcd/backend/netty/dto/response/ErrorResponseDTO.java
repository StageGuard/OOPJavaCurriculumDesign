package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.lang.reflect.Type;

public class ErrorResponseDTO {
    public String error;

    public ErrorResponseDTO(String result) {
        this.error = result;
    }

    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<ErrorResponseDTO> {
        @Override
        public JsonElement serialize(ErrorResponseDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", src.error);
            return jsonObject;
        }
    }
}
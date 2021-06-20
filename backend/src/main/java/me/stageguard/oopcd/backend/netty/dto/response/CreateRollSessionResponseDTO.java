package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;
import me.stageguard.oopcd.backend.netty.dto.IResponseDTO;

import java.lang.reflect.Type;

public class CreateRollSessionResponseDTO implements IResponseDTO {
    public String sessionKey;

    public CreateRollSessionResponseDTO(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<CreateRollSessionResponseDTO> {
        @Override
        public JsonElement serialize(CreateRollSessionResponseDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("sessionKey", src.sessionKey);
            return jsonObject;
        }
    }
}

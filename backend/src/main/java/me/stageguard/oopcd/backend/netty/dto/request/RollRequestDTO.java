package me.stageguard.oopcd.backend.netty.dto.request;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.io.StringReader;
import java.lang.reflect.Type;

public class RollRequestDTO {
    public String sessionKey = null;

    public static RollRequestDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), RollRequestDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<RollRequestDTO> {
        @Override
        public RollRequestDTO deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            RollRequestDTO obj = new RollRequestDTO();
            JsonObject jsonObject = json.getAsJsonObject();
            obj.sessionKey = jsonObject.get("sessionKey").getAsString();
            return obj;
        }
    }
}

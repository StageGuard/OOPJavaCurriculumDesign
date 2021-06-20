package me.stageguard.oopcd.backend.netty.dto.request;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;
import me.stageguard.oopcd.backend.roll.RollSession;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class CreateRollSessionRequestDTO {
    public RollSession.Config config = null;

    public static CreateRollSessionRequestDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), CreateRollSessionRequestDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<CreateRollSessionRequestDTO> {
        @Override
        public CreateRollSessionRequestDTO deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            CreateRollSessionRequestDTO obj = new CreateRollSessionRequestDTO();
            JsonObject jsonObject = json.getAsJsonObject();
            var configJSONElement = jsonObject.get("config");
            if (configJSONElement != null) {
                if (!configJSONElement.isJsonNull()) {
                    var config = configJSONElement.getAsJsonObject();
                    var layer = config.get("layer").getAsInt();
                    var ratioJArray = config.getAsJsonArray("ratio");
                    List<Double> ratioList = new java.util.ArrayList<>(Collections.emptyList());
                    for (var e : ratioJArray) {
                        ratioList.add(e.getAsDouble());
                    }
                    var transferCountJArray = config.getAsJsonArray("transferCount");
                    List<Integer> transferCountList = new java.util.ArrayList<>(Collections.emptyList());
                    for (var e : transferCountJArray) {
                        transferCountList.add(e.getAsInt());
                    }
                    var rollAlsoFromNextLayer = config.get("rollAlsoFromNextLayer").getAsBoolean();
                    obj.config = new RollSession.Config(layer, ratioList, transferCountList, rollAlsoFromNextLayer);
                }
            }
            return obj;
        }
    }
}

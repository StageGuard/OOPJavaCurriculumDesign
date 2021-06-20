package me.stageguard.oopcd.backend.netty.dto.request;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.stageguard.oopcd.backend.Pair;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.HashMap;

public class GetStudentsRequestDTO {
    public long limit;
    public HashMap<String, Pair<String, Object>> filterOpinions;

    public static GetStudentsRequestDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), GetStudentsRequestDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<GetStudentsRequestDTO> {
        @Override
        public GetStudentsRequestDTO deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            GetStudentsRequestDTO obj = new GetStudentsRequestDTO();
            JsonObject jsonObject = json.getAsJsonObject();
            obj.limit = jsonObject.get("limit").getAsLong();
            obj.filterOpinions = new HashMap<>();
            var filters = json.getAsJsonObject().getAsJsonArray("filter");
            for (var element : filters) {
                var filter = element.getAsJsonObject();
                var op = filter.get("op").getAsString();
                var value = filter.getAsJsonPrimitive("value");
                Integer assumeNumber = null;
                try {
                    assumeNumber = value.getAsInt();
                } catch (Exception ignored) {
                }
                if (assumeNumber == null) {
                    obj.filterOpinions.put(op, new Pair<>(
                            filter.get("key").getAsString(),
                            filter.get("value").getAsString()
                    ));
                } else {
                    obj.filterOpinions.put(op, new Pair<>(filter.get("key").getAsString(), assumeNumber));
                }

            }
            return obj;
        }
    }
}

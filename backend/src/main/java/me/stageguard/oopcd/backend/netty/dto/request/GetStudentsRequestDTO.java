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
            var filters = json.getAsJsonObject().getAsJsonObject("filter").entrySet();
            for (var filter : filters) {
                var expr = filter.getValue().getAsJsonObject();
                var arrV = expr.getAsJsonPrimitive("value");
                Integer assumeInt = null;
                try {
                    assumeInt = arrV.getAsInt();
                } catch (Exception ignored) {
                }
                if (assumeInt == null) {
                    obj.filterOpinions.put(filter.getKey(), new Pair<>(
                            expr.get("key").getAsString(),
                            expr.get("value").getAsString()
                    ));
                } else {
                    obj.filterOpinions.put(filter.getKey(), new Pair<>(expr.get("key").getAsString(), assumeInt));
                }

            }
            return obj;
        }
    }
}

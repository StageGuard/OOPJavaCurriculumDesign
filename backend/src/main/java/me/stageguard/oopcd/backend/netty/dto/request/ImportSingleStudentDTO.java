package me.stageguard.oopcd.backend.netty.dto.request;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.io.StringReader;
import java.lang.reflect.Type;

public class ImportSingleStudentDTO {
    public String name;
    public String id;

    public static ImportSingleStudentDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), ImportSingleStudentDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<ImportSingleStudentDTO> {
        @Override
        public ImportSingleStudentDTO deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            ImportSingleStudentDTO obj = new ImportSingleStudentDTO();
            JsonObject jsonObject = json.getAsJsonObject();
            obj.name = jsonObject.get("name").getAsString();
            obj.id = jsonObject.get("id").getAsString();
            return obj;
        }
    }
}
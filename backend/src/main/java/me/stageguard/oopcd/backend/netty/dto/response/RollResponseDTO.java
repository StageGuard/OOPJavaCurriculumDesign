package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.stageguard.oopcd.backend.database.dao.StudentDAO.StudentData;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;
import me.stageguard.oopcd.backend.netty.dto.IResponseDTO;

import java.lang.reflect.Type;

public class RollResponseDTO implements IResponseDTO {
    public StudentData student;

    public RollResponseDTO(StudentData student) {
        this.student = student;
    }

    @Override
    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<RollResponseDTO> {
        @Override
        public JsonElement serialize(RollResponseDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            JsonObject student = new JsonObject();
            student.addProperty("id", src.student.id);
            student.addProperty("name", src.student.name);
            student.addProperty("class", src.student.clazz);
            student.addProperty("totalAnswered", src.student.totalAnswered);
            student.addProperty("rightAnswered", src.student.rightAnswered);
            jsonObject.add("student", student);
            return jsonObject;
        }
    }
}

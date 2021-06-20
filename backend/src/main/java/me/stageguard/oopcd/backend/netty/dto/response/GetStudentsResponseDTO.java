package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.*;
import me.stageguard.oopcd.backend.database.dao.StudentDAO.StudentData;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;
import me.stageguard.oopcd.backend.netty.dto.IResponseDTO;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GetStudentsResponseDTO implements IResponseDTO {
    public ArrayList<StudentData> students;

    public GetStudentsResponseDTO(ArrayList<StudentData> students) {
        this.students = students;
    }

    @Override
    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<GetStudentsResponseDTO> {
        @Override
        public JsonElement serialize(GetStudentsResponseDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            JsonArray studentsArray = new JsonArray();
            for (var student : src.students) {
                var singleObj = new JsonObject();
                singleObj.addProperty("id", student.id);
                singleObj.addProperty("name", student.name);
                singleObj.addProperty("class", student.clazz);
                singleObj.addProperty("totalAnswered", student.totalAnswered);
                singleObj.addProperty("rightAnswered", student.rightAnswered);
                studentsArray.add(singleObj);
            }
            jsonObject.add("students", studentsArray);
            return jsonObject;
        }
    }
}
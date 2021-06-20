/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.netty.dto.request;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImportStudentsRequestDTO {
    public List<ImportSingleStudentRequestDTO> students;

    public static ImportStudentsRequestDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), ImportStudentsRequestDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<ImportStudentsRequestDTO> {
        @Override
        public ImportStudentsRequestDTO deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context
        ) throws JsonParseException {
            ImportStudentsRequestDTO obj = new ImportStudentsRequestDTO();
            obj.students = new ArrayList<>();
            var students = json.getAsJsonObject().getAsJsonArray("students");
            for (var single : students) {
                obj.students.add(ImportSingleStudentRequestDTO.deserialize(single.toString()));
            }
            return obj;
        }
    }
}
/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.netty.dto.request;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;

import java.io.StringReader;
import java.lang.reflect.Type;

public class SqlStatementRequestDTO {
    public String expression;

    public static SqlStatementRequestDTO deserialize(String data) {
        return GlobalGson.INSTANCE.fromJson(new JsonReader(new StringReader(data)), SqlStatementRequestDTO.class);
    }

    public static class Deserializer implements JsonDeserializer<SqlStatementRequestDTO> {
        @Override
        public SqlStatementRequestDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            SqlStatementRequestDTO obj = new SqlStatementRequestDTO();
            JsonObject jsonObject = json.getAsJsonObject();
            obj.expression = jsonObject.get("expression").getAsString();
            return obj;
        }
    }
}

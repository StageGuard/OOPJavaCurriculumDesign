/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.netty.dto.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.stageguard.oopcd.backend.netty.dto.GlobalGson;
import me.stageguard.oopcd.backend.netty.dto.IResponseDTO;

import java.lang.reflect.Type;

public class ErrorResponseDTO implements IResponseDTO {
    public String error;

    public ErrorResponseDTO(String result) {
        this.error = result;
    }

    @Override
    public String serialize() {
        return GlobalGson.INSTANCE.toJson(this);
    }

    public static class Serializer implements JsonSerializer<ErrorResponseDTO> {
        @Override
        public JsonElement serialize(ErrorResponseDTO src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", src.error);
            return jsonObject;
        }
    }
}
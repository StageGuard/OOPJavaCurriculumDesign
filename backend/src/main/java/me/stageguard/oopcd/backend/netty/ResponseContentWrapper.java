/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.netty;

import io.netty.handler.codec.http.HttpResponseStatus;
import me.stageguard.oopcd.backend.netty.dto.IResponseDTO;

public class ResponseContentWrapper {
    private final HttpResponseStatus status;
    private final IResponseDTO content;

    public ResponseContentWrapper(HttpResponseStatus status, IResponseDTO content) {
        this.status = status;
        this.content = content;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public String getContent() {
        return content.serialize();
    }
}

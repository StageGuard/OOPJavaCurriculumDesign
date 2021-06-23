/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import me.stageguard.oopcd.backend.netty.dto.response.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class HttpAuthorizeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAuthorizeHandler.class);
    private final String mAuthKey;

    public HttpAuthorizeHandler(String mAuthKey) {
        this.mAuthKey = mAuthKey;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        if (mAuthKey != null) {
            var authorization = msg.headers().getAsString("Authorization");
            if (authorization == null) {
                ctx.writeAndFlush(unauthorized("Missing header \"Authorization\", please check your headers."));
                ctx.close();
                return;
            }
            if (!authorization.equals(mAuthKey)) {
                ctx.writeAndFlush(unauthorized("Authorize failed, please check your headers."));
                ctx.close();
                return;
            }
        } else {
            LOGGER.warn("AuthKey is not set, server may suffer from illegal call.");
        }
        ReferenceCountUtil.retain(msg);
        ctx.fireChannelRead(msg);
        ctx.flush();
    }

    private DefaultFullHttpResponse unauthorized(String msg) {
        var response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.UNAUTHORIZED,
                Unpooled.wrappedBuffer((new ErrorResponseDTO(msg)).serialize().getBytes())
        );
        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        return response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.info("exceptionCaught in HttpAuthorizeHandler");
        if (null != cause) {
            cause.printStackTrace();
        }
        if (null != ctx) {
            ctx.close();
        }
    }
}
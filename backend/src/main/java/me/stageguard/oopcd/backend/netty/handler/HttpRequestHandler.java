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
import me.stageguard.oopcd.backend.netty.IRouteHandler;
import me.stageguard.oopcd.backend.netty.Route;
import me.stageguard.oopcd.backend.netty.RouteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandler.class);

    private ArrayList<? extends IRouteHandler> mHandlers = new ArrayList<>();

    public void setHandlers(ArrayList<IRouteHandler> handlers) {
        this.mHandlers = handlers;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        DefaultFullHttpResponse response = null;
        for(IRouteHandler h : mHandlers) {
            var route = h.getClass().getAnnotation(Route.class);
            if(route != null) {
                var method = route.method();
                var path = route.path();
                if((msg.method().toString().equals(method) || method.equals(RouteType.COMPOUND)) && msg.uri().equals(path)) {
                    try {
                        var handled = h.handle(msg);
                        response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                handled.getStatus(),
                                Unpooled.wrappedBuffer(handled.getContent().getBytes()));
                    } catch (Exception ex) {
                        response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                                Unpooled.wrappedBuffer(("{\"error\":\"" + ex + "\"}").getBytes()));
                    }
                    break;
                }
            } else {
                var errorMsg = "Annotation \"Route\" is missing for class "
                        + h.getClass().getName()
                        + " which implements IRouteHandler.";
                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.INTERNAL_SERVER_ERROR,
                        Unpooled.wrappedBuffer(("{\"error\":\"" + errorMsg + "\"}").getBytes()));
                break;
            }
        }
        if (response == null) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.NOT_FOUND,
                    Unpooled.wrappedBuffer(("{\"error\":\"Request path "
                            + msg.uri()
                            + " is not found.\"}").getBytes())
            );
        }
        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.info("exceptionCaught in HttpRequestHandler");
        if(null != cause) cause.printStackTrace();
        if(null != ctx) ctx.close();
    }
}
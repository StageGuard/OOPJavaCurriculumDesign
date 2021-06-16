package me.stageguard.oopcd.backend.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import me.stageguard.oopcd.backend.netty.handler.HttpAuthorizeHandler;
import me.stageguard.oopcd.backend.netty.handler.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class NettyHttpServer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpServer.class);

    private final int port;
    private final HttpRequestHandler httpRequestHandler;
    private final HttpAuthorizeHandler httpAuthorizeHandler;

    private final ServerBootstrap bootstrap;

    private NettyHttpServer(NettyHttpServerBuilder builder) {
        this.port = builder.port;
        bootstrap = new ServerBootstrap();

        httpAuthorizeHandler = new HttpAuthorizeHandler(builder.authKey);
        httpRequestHandler = new HttpRequestHandler();
        httpRequestHandler.setHandlers(builder.handlers);

        bootstrap
            .group(new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline()
                        .addLast("decoder", new HttpRequestDecoder())
                        .addLast("encoder", new HttpResponseEncoder())
                        .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                        .addLast("authHandler", httpAuthorizeHandler)
                        .addLast("routeHandler", httpRequestHandler);
                    LOGGER.info("Channel initialized: " + ch);
                }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        LOGGER.info("Bootstrap initialized: " + bootstrap);
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Starting Netty HTTP Server...");
            bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }
    }

    public static class NettyHttpServerBuilder {
        private int port;
        private String authKey;
        private ArrayList<IRouteHandler> handlers = null;

        private NettyHttpServerBuilder(int port) {
            this.port = port;
        }

        public static NettyHttpServerBuilder create(int port) {
            return new NettyHttpServerBuilder(port);
        }

        public NettyHttpServerBuilder port(int port) {
            this.port = port;
            return this;
        }

        public NettyHttpServerBuilder route(ArrayList<IRouteHandler> handlers) {
            this.handlers = handlers;
            return this;
        }

        public NettyHttpServerBuilder authKey(String authKey) {
            this.authKey = authKey;
            return this;
        }

        public NettyHttpServer build() {
            return new NettyHttpServer(this);
        }
    }
}
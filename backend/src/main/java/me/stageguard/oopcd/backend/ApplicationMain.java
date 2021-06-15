package me.stageguard.oopcd.backend;

import me.stageguard.oopcd.backend.netty.NettyHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMain.class);

    public static void main(String[] args) {
        var server = new NettyHttpServer(8081);
        try {
            server.start();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }
}

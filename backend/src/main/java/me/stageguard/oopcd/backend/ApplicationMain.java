package me.stageguard.oopcd.backend;

import me.stageguard.oopcd.backend.netty.NettyHttpServer.NettyHttpServerBuilder;
import me.stageguard.oopcd.backend.database.Database.DatabaseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMain.class);

    private static final ExecutorService executors = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        var nettyHttpService = NettyHttpServerBuilder.create(8081)
            .route(new ArrayList<>(

            ))
        .build();
        executors.execute(nettyHttpService);
        var databaseService = DatabaseBuilder.create("localhost", 3306)
            .username("root")
            .password("testpwd")
            .database("oopcd_database")
        .build();
        executors.execute(databaseService);
    }
}

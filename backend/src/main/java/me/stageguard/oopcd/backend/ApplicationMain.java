/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend;

import me.stageguard.oopcd.backend.database.Database.DatabaseBuilder;
import me.stageguard.oopcd.backend.netty.NettyHttpServer.NettyHttpServerBuilder;
import me.stageguard.oopcd.backend.netty.route.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author StageGuard
 */
public class ApplicationMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMain.class);
    private static int SERVER_PORT = 8081;
    private static String AUTH_KEY = "114514_1919810";
    private static String DATABASE_ADDRESS = "localhost";
    private static int DATABASE_PORT = 3306;
    private static String DATABASE_USERNAME = "root";
    private static String DATABASE_PASSWORD = "testpwd";
    private static String DATABASE_TABLE = "oopcd_database";

    private static final ExecutorService EXECUTORS = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), r -> {
        var thread = new Thread(r);
        thread.setName("main-thread-" + LocalDate.now());
        return thread;
    }, new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        parseJvmArguments();
        var nettyHttpService = NettyHttpServerBuilder.create(SERVER_PORT)
                .route(new ArrayList<>(Arrays.asList(
                        new TestExecuteQueryRoute(),
                        new ImportSingleStudentRoute(),
                        new ImportStudentsRoute(),
                        new GetStudentsRoute(),
                        new CreateRollSessionRoute(),
                        new RollRoute(),
                        new AnswerRoute()
                )))
                .authKey(AUTH_KEY)
                .build();
        EXECUTORS.execute(nettyHttpService);
        var databaseService = DatabaseBuilder.create(DATABASE_ADDRESS, DATABASE_PORT)
                .username(DATABASE_USERNAME)
                .password(DATABASE_PASSWORD)
                .database(DATABASE_TABLE)
                .build();
        EXECUTORS.execute(databaseService);
        LOGGER.info("Server is running at localhost:" + SERVER_PORT + " , auth key is " + AUTH_KEY);
        LOGGER.info("Server will connect database at " + DATABASE_ADDRESS + ":" + DATABASE_PORT);
        EXECUTORS.shutdown();
    }

    private static void parseJvmArguments() {
        try {
            SERVER_PORT = Integer.parseInt(System.getProperty("sg.oopcd.backend.server.port"));
        } catch (Exception ignored) {
        }
        try {
            var property = System.getProperty("sg.oopcd.backend.server.authKey");
            if (property != null) AUTH_KEY = property;
        } catch (Exception ignored) {
        }
        try {
            var property = System.getProperty("sg.oopcd.backend.database.address");
            if (property != null) DATABASE_ADDRESS = property;
        } catch (Exception ignored) {
        }
        try {
            DATABASE_PORT = Integer.parseInt(System.getProperty("sg.oopcd.backend.database.port"));
        } catch (Exception ignored) {
        }
        try {
            var property = System.getProperty("sg.oopcd.backend.database.username");
            if (property != null) DATABASE_USERNAME = property;
        } catch (Exception ignored) {
        }
        try {
            var property = System.getProperty("sg.oopcd.backend.database.password");
            if (property != null) DATABASE_PASSWORD = property;
        } catch (Exception ignored) {
        }
        try {
            var property = System.getProperty("sg.oopcd.backend.database.table");
            if (property != null) DATABASE_TABLE = property;
        } catch (Exception ignored) {
        }
    }
}

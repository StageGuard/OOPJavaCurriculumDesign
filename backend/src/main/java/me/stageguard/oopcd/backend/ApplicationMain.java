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

    private static final ExecutorService EXECUTORS = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), r -> {
        var thread = new Thread(r);
        thread.setName("main-thread-" + LocalDate.now());
        return thread;
    }, new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        var nettyHttpService = NettyHttpServerBuilder.create(8088)
                .route(new ArrayList<>(Arrays.asList(
                        new TestExecuteQueryRoute(),
                        new ImportSingleStudentRoute(),
                        new ImportStudentsRoute(),
                        new GetStudentsRoute(),
                        new CreateRollSessionRoute(),
                        new RollRoute(),
                        new AnswerRoute()
                )))
                .authKey("114514_1919810")
                .build();
        EXECUTORS.execute(nettyHttpService);
        var databaseService = DatabaseBuilder.create("localhost", 3306)
                .username("root")
                .password("testpwd")
                .database("oopcd_database")
                .build();
        EXECUTORS.execute(databaseService);
        EXECUTORS.shutdown();
    }
}

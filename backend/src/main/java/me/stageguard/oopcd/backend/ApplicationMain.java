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
import me.stageguard.oopcd.backend.netty.route.GetStudentsRoute;
import me.stageguard.oopcd.backend.netty.route.ImportSingleStudentRoute;
import me.stageguard.oopcd.backend.netty.route.ImportStudentsRoute;
import me.stageguard.oopcd.backend.netty.route.TestExecuteQueryRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationMain.class);

    private static final ExecutorService executors = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        var nettyHttpService = NettyHttpServerBuilder.create(8081)
                .route(new ArrayList<>(Arrays.asList(
                        new TestExecuteQueryRoute(),
                        new ImportSingleStudentRoute(),
                        new ImportStudentsRoute(),
                        new GetStudentsRoute()
                )))
                .authKey("114514")
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

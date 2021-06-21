/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class Database implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);
    public static Database INSTANCE;

    private HikariDataSource hikariDataSource;
    private final DatabaseBuilder builder;

    private Database(DatabaseBuilder builder) {
        this.builder = builder;
    }

    private HikariDataSource hikariDataSourceProvider(DatabaseBuilder builder) {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + builder.ip + "/" + builder.database);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(builder.username);
        if(builder.password != null) config.setPassword(builder.password);
        config.setMaximumPoolSize(builder.maxPoolSize == null ? 10 : builder.maxPoolSize);
        return new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    public static void queryBlocking(String statement, Consumer<Optional<ResultSet>> consumer) throws SQLException {
        LOGGER.info("Execute: " + statement);
        if (INSTANCE == null) {
            LOGGER.error("Database hasn't been initialized, query operation will not execute.");
            consumer.accept(Optional.empty());
        }
        try (var connection = INSTANCE.getConnection()) {
            var connectionStatement = connection.createStatement();
            consumer.accept(Optional.of(connectionStatement.executeQuery(statement)));
        } catch (SQLException ex) {
            throw new SQLException("Error occurred while querying: " + ex);
        }
    }

    public static int executeBlocking(String statement) throws SQLException {
        LOGGER.info("Execute: " + statement);
        if (INSTANCE == null) {
            LOGGER.error("Database hasn't been initialized, query operation will not execute.");
            return 0;
        }
        int result;
        try (var connection = INSTANCE.getConnection();) {
            var connectionStatement = connection.createStatement();
            result = connectionStatement.executeUpdate(statement);
        } catch (SQLException ex) {
            throw new SQLException("Error occurred while querying: " + ex);
        }
        return result;
    }

    @Override
    public void run() {
        hikariDataSource = hikariDataSourceProvider(builder);
    }

    public static class DatabaseBuilder {
        private String ip;
        private int port;
        private String database;
        private String username;
        private String password;
        private Integer maxPoolSize;

        private DatabaseBuilder(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public static DatabaseBuilder create(String ip, int port) {
            return new DatabaseBuilder(ip, port);
        }

        public DatabaseBuilder port(int port) {
            this.port = port;
            return this;
        }

        public DatabaseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public DatabaseBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DatabaseBuilder database(String database) {
            this.database = database;
            return this;
        }

        public DatabaseBuilder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public Database build() {
            if(INSTANCE == null) {
                if(database == null) throw new IllegalArgumentException("Property database cannot be null.");
                if(username == null) throw new IllegalArgumentException("Property username cannot be null.");
                var newInstance = new Database(this);
                INSTANCE = newInstance;
                return newInstance;
            } else {
                LOGGER.warn("Single Object Database has existed, will not build a new instance.");
                return INSTANCE;
            }
        }
    }
}

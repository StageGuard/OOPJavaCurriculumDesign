package me.stageguard.oopcd.backend.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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

    public static Optional<ResultSet> queryBlocking(String statement) {
        LOGGER.info("Execute: " + statement);
        if(INSTANCE == null) {
            LOGGER.error("Database hasn't been initialized, query operation will not execute.");
            return Optional.empty();
        }
        try {
            var connection = INSTANCE.getConnection();
            var connectionStatement = connection.createStatement();
            return Optional.of(connectionStatement.executeQuery(statement));
        } catch (SQLException ex) {
            LOGGER.error("Error occurred while querying: " + ex);
            return Optional.empty();
        }
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

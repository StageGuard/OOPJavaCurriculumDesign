package me.stageguard.oopcd.backend.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import me.stageguard.oopcd.backend.database.AbstractDataAccessObject.IDataAccessObjectData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unused", "DuplicatedCode"})
public abstract class AbstractDataAccessObject<T extends IDataAccessObjectData> {
    private final String tableName;
    private final Logger LOGGER;

    protected AbstractDataAccessObject(String tableName) {
        this.tableName = tableName;
        LOGGER = LoggerFactory.getLogger(typeOfT());
    }

    public int create() throws SQLException {
        var dataFields = typeOfT().getFields();
        var statement = new StringBuilder();
        statement.append("CREATE TABLE IF NOT EXISTS ")
            .append("`").append(tableName).append("`").append(" ")
            .append("(").append("\n");
        for(var i = 0; i < dataFields.length; i ++) {
            var property = dataFields[i].getAnnotation(FieldProperty.class);
            if(property != null) {
                statement.append("`").append(property.name()).append("`").append(" ");
                statement.append(property.type()).append(" ");
                if(property.zerofill()) statement.append("ZEROFILL").append(" ");
                if(property.unsigned()) statement.append("UNSIGNED").append(" ");
                statement.append(property.nullable() ? "NULL" : "NOT NULL").append(" ");
                if(!property.defaultV().equals("")) {
                    if(property.defaultV().equalsIgnoreCase("null") && !property.nullable()) {
                        throw new IllegalArgumentException("Field " + dataFields[i].getName() + " shouldn't be null but the default value is null");
                    }
                    statement.append("DEFAULT ").append(property.defaultV()).append(" ");
                }
                if(property.order() != FieldProperty.FieldOrder.DEFAULT) {
                    if(property.after().equals("")) {
                        throw new IllegalArgumentException("Order is null since order() is not FieldOrder.DEFAULT");
                    }
                    statement.append(property.order() == FieldProperty.FieldOrder.FIRST ? "FIRST" : ("AFTER " + property.after()));
                }
                statement.append(i == dataFields.length - 1 ? "\n" : ",\n");
            }
        }
        statement.append(") COLLATE=`utf8_general_ci`;");
        LOGGER.info(statement.toString());
        try {
            return execute(statement.toString());
        } catch (Exception ex) {
            throw new SQLException("SQL execution error in create: " + ex);
        }
    }

    public int update(T data) throws SQLException {
        var initial = data.getInitialValue();
        if(initial == null) {
            throw new SQLException("Cannot update this item because initialValue is null, maybe instance is not created internally.");
        }
        var kvMapping = data.kv();
        var statement = new StringBuilder();
        statement.append("UPDATE `").append(tableName).append("` ");
        statement.append("SET ");
        AtomicInteger index = new AtomicInteger();
        kvMapping.forEach((k, v) -> {
            statement.append("`").append(k).append("`=");
            statement.append("`").append(v).append("`");
            if(index.get() != kvMapping.size() - 1) statement.append(",");
            statement.append(" ");
            index.getAndIncrement();
        });
        statement.append("WHERE ");
        index.set(0);
        initial.forEach((k, v) -> {
            statement.append("`").append(k).append("`=");
            String type = "varchar";
            try {
                type = typeOfT()
                    .getField(Objects.requireNonNull(getFieldViaPropertyName(k)))
                    .getAnnotation(FieldProperty.class).type().toLowerCase();
            } catch (NoSuchFieldException ignored) { }
            if(type.contains("int") ||
                    type.contains("decimal") ||
                    type.contains("double") ||
                    type.contains("float") || type.contains("bit")
            ) {
                statement.append("`").append(v).append("` ");
            } else {
                statement.append(v);
            }
            if(index.get() != initial.size() - 1) statement.append("AND");
            statement.append(" ");
            index.getAndIncrement();
        });
        statement.append("LIMIT 1;");
        LOGGER.info(statement.toString());
        try {
            return execute(statement.toString());
        } catch (Exception ex) {
            throw new SQLException("SQL execution error in update: " + ex);
        }
    }

    public ArrayList<T> retrieve(ConditionFilterSAM filterSAM) throws SQLException {
        return retrieve(filterSAM, 100L);
    }

    public ArrayList<T> retrieve(ConditionFilterSAM filterSAM, long limit) throws SQLException {
        var statement = new StringBuilder();
        var filter = filterSAM.invoke(new ConditionFilter());
        statement.append("SELECT * FROM `").append(tableName).append("`");
        if(!filter.isEmpty()) {
            statement.append(" WHERE ");
            statement.append(filter);
            statement.append(" ");
        }
        statement.append("LIMIT ");
        statement.append(limit);
        statement.append(";");
        LOGGER.info(statement.toString());
        var result = query(statement.toString());
        if(result.isEmpty()) {
            return new ArrayList<>();
        } else {
            var list = new ArrayList<T>();
            var resultSet = result.get();
            while (resultSet.next()) {
                list.add(serialize(resultSet));
            }
            return list;
        }
    }

    public int delete(ConditionFilterSAM filterSAM) throws SQLException {
        return delete(filterSAM, 100L);
    }

    public int delete(ConditionFilterSAM filterSAM, long limit) throws SQLException {
        var statement = new StringBuilder();
        var filter = filterSAM.invoke(new ConditionFilter());
        statement.append("DELETE FROM `").append(tableName).append("`");
        if (!filter.isEmpty()) {
            statement.append(" WHERE ");
            statement.append(filter);
            statement.append(" ");
        }
        statement.append("LIMIT ");
        statement.append(limit);
        statement.append(";");
        LOGGER.info(statement.toString());
        return execute(statement.toString());
    }

    private String getFieldViaPropertyName(String name) {
        for (var f : typeOfT().getFields()) {
            if(f.getAnnotation(FieldProperty.class).name().equals(name)) {
                return f.getName();
            }
        }
        return null;
    }

    private int execute(String statement) {
        return Database.executeBlocking(statement);
    }

    private Optional<ResultSet> query(String statement) {
        return Database.queryBlocking(statement);
    }

    protected abstract Class<T> typeOfT();
    protected abstract T serialize(ResultSet resultSet) throws SQLException;

    protected abstract static class IDataAccessObjectData {
        private Map<String, Object> initialValue = null;
        protected IDataAccessObjectData(Map<String, Object> initial) {
            if(initial != null) initialValue = initial;
        }
        public Map<String, Object> getInitialValue() {
            return initialValue;
        }
        protected abstract Map<String, Object> kv();
    }
}

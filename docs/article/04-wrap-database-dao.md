### Backend: 封装数据库 DAO 模式

```java
public abstract class AbstractDataAccessObject<T extends AbstractDataAccessObjectData> {
    private final String tableName;

    protected AbstractDataAccessObject(String tableName) {
        this.tableName = tableName;
    }

    public int create() throws SQLException {
        var dataFields = typeOfT().getFields();
        var statement = new StringBuilder();
        statement.append("CREATE TABLE IF NOT EXISTS ")
        /* build sql statement */
        statement.append(") COLLATE=`utf8_general_ci`;");
        try {
            return execute(statement.toString());
        } catch (Exception ex) {
            throw new SQLException("SQL execution error in create: " + ex);
        }
    }

    public int insert(T item) throws SQLException {
        return insert(new ArrayList<>(Collections.singletonList(item)));
    }

    public int insert(List<T> itemList) throws SQLException {
        if (itemList.isEmpty()) {
            throw new SQLException("Nothing to insert because list is empty.");
        }
        var statement = new StringBuilder();
        statement.append("INSERT INTO `").append(tableName).append("` ");
        /* build sql statement */
        statement.append(";");
        try {
            return execute(statement.toString());
        } catch (Exception ex) {
            throw new SQLException("SQL execution error in create: " + ex);
        }
    }

    public int update(T data) throws SQLException {
        var initial = data.getInitialValue();
        if (initial == null) {
            throw new SQLException("Cannot update this item because initialValue is null, maybe instance is not created internally.");
        }
        var kvMapping = data.deserialize();
        var statement = new StringBuilder();
        statement.append("UPDATE `").append(tableName).append("` ");
        /* build sql statement */
        statement.append("LIMIT 1;");
        try {
            var executeResult = execute(statement.toString());
            data.resetInitialValue();
            return executeResult;
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
        /* build sql statement */
        statement.append(";");
        var list = new ArrayList<T>();
        try {
            query(statement.toString(), result -> {
                if (result.isEmpty()) {
                    return;
                }
                var resultSet = result.get();
                while (resultSet.next()) {
                    list.add(serialize(resultSet));
                }
            });
        } catch (SQLException ex) {
            throw new SQLException("SQL execution error in create: " + ex);
        }
        return list;
    }

    public int delete(ConditionFilterSAM filterSAM) throws SQLException {
        return delete(filterSAM, 100L);
    }

    public int delete(ConditionFilterSAM filterSAM, long limit) throws SQLException {
        var statement = new StringBuilder();
        var filter = filterSAM.invoke(new ConditionFilter());
        statement.append("DELETE FROM `").append(tableName).append("`");
        /* build sql statement */
        statement.append(";");
        try {
            return execute(statement.toString());
        } catch (Exception ex) {
            throw new SQLException("SQL execution error in create: " + ex);
        }
    }

    private String getFieldViaPropertyName(String name) {
        for (var f : typeOfT().getFields()) {
            if (f.getAnnotation(FieldProperty.class).name().equals(name)) {
                return f.getName();
            }
        }
        return null;
    }

    private boolean isFieldANumber(String type) {
        return type.contains("int") ||
                type.contains("decimal") ||
                type.contains("double") ||
                type.contains("float") || type.contains("bit");
    }

    private int execute(String statement) throws SQLException {
        return Database.executeBlocking(statement);
    }

    private void query(
            String statement,
            ConsumerOrException<Optional<ResultSet>, SQLException> consumer
    ) throws SQLException {
        Database.queryBlocking(statement, consumer);
    }

    protected abstract Class<T> typeOfT();

    protected abstract T serialize(ResultSet resultSet) throws SQLException;

    protected abstract static class AbstractDataAccessObjectData {
        private Map<String, Object> initialValue = null;

        protected AbstractDataAccessObjectData(Map<String, Object> initial) {
            if (initial != null) {
                initialValue = initial;
            }
        }

        public Map<String, Object> getInitialValue() {
            return initialValue;
        }

        public void resetInitialValue() {
            initialValue = deserialize();
        }

        protected abstract Map<String, Object> deserialize();
    }
}
```

在 `AbstractDataAccessObject` 中，核心方法为 `create`，`update`，`retrive` 和 `delete`，他们是实际执行数据库 CURD 的操作。

在创建 `AbstractDataAccessObject` 时，需要接收一个继承 `AbstractDataAccessObjectData` 的泛型类，`AbstractDataAccessObjectData`
是实际存储数据的类，他需要实现 `deserialize()` 方法来获取原始数据用于构建 SQL 语句。

`AbstractDataAccessObjectData` 还具有状态记录功能，即在改变类中属性的值后仍可以方便地通过 `update(T data)` 来更新数据。

#### 关于数据在数据库中的属性，则通过一个 Annotation 来定义

```java
@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FieldProperty {
    String name();
    String type();
    boolean unsigned() default false;
    boolean zerofill() default false;
    boolean nullable() default true;
    String defaultV() default "";
    int order() default FieldOrder.DEFAULT;
    String after() default "";
    boolean prime() default false;
    class FieldOrder {
        public final static int DEFAULT = 0;
        public final static int FIRST = 1;
        public final static int AFTER = 2;

    }
}
```

这里可以设置常见的列表项目属性。

通过 `AbstractDataAccessObject` 和 `AbstractDataAccessObjectData` 的抽象，使 DAO 的定义变得非常便捷。

例如记录学生信息的表：

```java
public class StudentDAO extends AbstractDataAccessObject<StudentDAO.StudentData> {
    public static final StudentDAO INSTANCE = new StudentDAO();

    private StudentDAO() { super("students"); }

    @Override
    protected Class<StudentData> typeOfT() { return StudentData.class; }

    @Override
    public StudentData serialize(ResultSet resultSet) throws SQLException {
        return new StudentData(
                resultSet.getString(1),
                resultSet.getLong(2),
                resultSet.getString(3),
                resultSet.getInt(4),
                resultSet.getInt(5)
        );
    }

    public static class StudentData extends AbstractDataAccessObjectData {
        @FieldProperty(name = "name", type = "varchar(10)")
        public String name;
        @FieldProperty(name = "id", type = "bigint", prime = true)
        public long id;
        @FieldProperty(name = "class", type = "varchar(50)")
        public String clazz;
        @FieldProperty(name = "totalAnswered", type = "int", defaultV = "0")
        public int totalAnswered;
        @FieldProperty(name = "rightAnswered", type = "int", defaultV = "0")
        public int rightAnswered;

        public StudentData() {  super(null); }

        protected StudentData(String name, long id, String clazz, int totalAnswered, int rightAnswered) {
            super(Map.of(
                    "name", name,
                    "id", id,
                    "class", clazz,
                    "totalAnswered", totalAnswered,
                    "rightAnswered", rightAnswered
            ));
            this.name = name;
            this.id = id;
            this.clazz = clazz;
            this.totalAnswered = totalAnswered;
            this.rightAnswered = rightAnswered;
        }

        public StudentData(String name, long id, String clazz) {
            this(name, id, clazz, 0, 0);
        }

        @Override
        protected Map<String, Object> deserialize() {
            return Map.of(
                    "name", name,
                    "id", id,
                    "class", clazz,
                    "totalAnswered", totalAnswered,
                    "rightAnswered", rightAnswered
            );
        }

        @Override
        public boolean equals(Object o) { /*idea generated code*/ }

        @Override
        public int hashCode() { /*idea generated code*/ }
    }
}
```

#### 使用实例：

```java
@Route(path = "/v1/importSingleStudent", method = RouteType.POST)
public class ImportSingleStudentRoute implements IRouteHandler {
    @Override
    public ResponseContentWrapper handle(FullHttpRequest request) {
        var content = request.content().toString(StandardCharsets.UTF_8);
        var stuImport = ImportSingleStudentRequestDTO.deserialize(content);
        try {
            StudentDAO.INSTANCE.create();
            int sqlResult = StudentDAO.INSTANCE.insert(new StudentData(stuImport.name, stuImport.id, stuImport.clazz));
            return new ResponseContentWrapper(HttpResponseStatus.OK, new SqlExecuteResponseDTO(sqlResult));
        } catch (Exception ex) {
            return new ResponseContentWrapper(HttpResponseStatus.OK, new ErrorResponseDTO(ex.toString()));
        }
    }
}
```

反序列化请求后直接调用 `StudentDAO.INSTANCE.insert(new StudentData(stuImport.name, stuImport.id, stuImport.clazz));` 即可插入数据。

```java
public class RollSession() {
	/* fields */
    public void answerRight() throws SQLException {
        if (currentStudent != null) {
            haveRolled.add(currentStudent);
            currentStudent.totalAnswered++;
            currentStudent.rightAnswered++;
            StudentDAO.INSTANCE.update(currentStudent);
            currentLayerUnansweredCount++;
            currentStudent = null;
        } else {
            throw new UnsupportedOperationException("Please roll first!");
        }
    }
}
```

`currentStudent` 是一个 `StudentData`，得益于`AbstractDataAccessObjectData`
的状态记录功能，在更改他的属性值后直接调用 `StudentDAO.INSTANCE.update(currentStudent);` 即可更新数据。

> 返回主页：[OOPJavaCurriculumDesign](../index.md)
>
> 下一篇：[Backend: 基于状态点名实现](05-roll.md)


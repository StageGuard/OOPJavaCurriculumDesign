package me.stageguard.oopcd.backend.database.dao;

import me.stageguard.oopcd.backend.database.AbstractDataAccessObject;
import me.stageguard.oopcd.backend.database.FieldProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class StudentDAO extends AbstractDataAccessObject<StudentDAO.StudentData> {
    public static final StudentDAO INSTANCE = new StudentDAO();

    private StudentDAO() {
        super("students");
    }

    @Override
    protected Class<StudentData> typeOfT() {
        return StudentData.class;
    }

    @Override
    public StudentData serialize(ResultSet resultSet) throws SQLException {
       return new StudentData(
           resultSet.getString(1),
           resultSet.getLong(2),
           resultSet.getString(3)
       );
    }

    public static class StudentData extends IDataAccessObjectData {
        /**
         * 学生姓名
         */
        @FieldProperty(name = "name", type = "int", unsigned = true, defaultV = "1")
        public String name;
        /**
         * 学生学号
         */
        @FieldProperty(name = "id", type = "bigint")
        public long id;
        /**
         * 学生班级
         */
        @FieldProperty(name = "class", type = "varchar(50)")
        public String clazz;

        public StudentData() {
            super(null);
        }

        public StudentData(String name, long id, String clazz) {
            super(Map.of("name", name, "id", id, "class", clazz));
            this.name = name;
            this.id = id;
            this.clazz = clazz;
        }

        @Override
        protected Map<String, Object> deserialize() {
            return Map.of("name", name, "id", id, "class", clazz);
        }
    }
}

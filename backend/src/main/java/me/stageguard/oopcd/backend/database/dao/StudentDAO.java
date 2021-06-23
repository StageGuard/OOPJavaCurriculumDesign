/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.database.dao;

import me.stageguard.oopcd.backend.database.AbstractDataAccessObject;
import me.stageguard.oopcd.backend.database.FieldProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

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

        public StudentData() {
            super(null);
        }

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
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StudentData that = (StudentData) o;
            return id == that.id && totalAnswered == that.totalAnswered && rightAnswered == that.rightAnswered && name.equals(that.name) && clazz.equals(that.clazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, id, clazz, totalAnswered, rightAnswered);
        }
    }
}

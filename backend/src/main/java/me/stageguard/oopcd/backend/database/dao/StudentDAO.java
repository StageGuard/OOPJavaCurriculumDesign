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
        @FieldProperty(name = "name", type = "varchar(10)")
        public String name;
        @FieldProperty(name = "id", type = "bigint", prime = true)
        public long id;
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

/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

import me.stageguard.oopcd.backend.database.ConditionFilter.Condition;
import me.stageguard.oopcd.backend.database.dao.StudentDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TestDAO {
    public static void main(String[] args) throws SQLException {
        var cre = StudentDAO.INSTANCE.create();
        var testData = new StudentDAO.StudentData("myName", 114514L, "一班");
        testData.name = "mySecondName";
        StudentDAO.INSTANCE.update(testData);
        StudentDAO.INSTANCE.retrieve(filter -> filter
            .head(Condition.eq("name", "mySecondName"))
            .and(Condition.eq("clazz", "一班"))
            .and(Condition.isNull("id"))
            .and(Condition.greater("num", 12))
        );
        StudentDAO.INSTANCE.insert(new ArrayList<>(Arrays.asList(
                new StudentDAO.StudentData("野兽先辈", 114514L, "114514班"),
                new StudentDAO.StudentData("蔡徐坤", 5211314L, "鸡班"),
                new StudentDAO.StudentData("VanSama", 111222333L, "哲学班")
        )));
    }
}

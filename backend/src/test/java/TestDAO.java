import me.stageguard.oopcd.backend.database.ConditionFilter;
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

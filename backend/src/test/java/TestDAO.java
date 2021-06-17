import me.stageguard.oopcd.backend.database.ConditionFilter;
import me.stageguard.oopcd.backend.database.ConditionFilter.Condition;
import me.stageguard.oopcd.backend.database.dao.StudentDAO;

import java.sql.SQLException;

public class TestDAO {
    public static void main(String[] args) throws SQLException {
        var cre = StudentDAO.INSTANCE.create();
        var testData = new StudentDAO.StudentData("myName", 114514L, "123");
        testData.name = "mySecondName";
        StudentDAO.INSTANCE.update(testData);
        StudentDAO.INSTANCE.retrieve(filter -> filter
            .head(Condition.eq("name", "mySecondName"))
            .and(Condition.eq("clazz", "1班"))
            .and(Condition.isNull("id"))
            .and(Condition.greater("num", 12))
        );
    }
}

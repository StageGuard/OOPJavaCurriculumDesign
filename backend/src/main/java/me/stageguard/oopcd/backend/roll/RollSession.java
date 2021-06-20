package me.stageguard.oopcd.backend.roll;

import me.stageguard.oopcd.backend.database.ConditionFilter.Condition;
import me.stageguard.oopcd.backend.database.dao.StudentDAO;
import me.stageguard.oopcd.backend.database.dao.StudentDAO.StudentData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RollSession {
    private final Config config;
    private final Random random;
    private final ArrayList<StudentData> haveRolled;
    private int currentLayer;
    private int currentLayerUnansweredCount;
    private StudentData currentStudent = null;

    public RollSession(Config config) {
        this.config = config;
        currentLayer = 1;
        currentLayerUnansweredCount = 0;
        random = new Random();
        haveRolled = new ArrayList<>();
    }

    public StudentData roll() throws SQLException {
        //上一次 roll 未确认是否答对，按未达对处理。
        if (currentStudent != null) {
            haveRolled.add(currentStudent);
            currentStudent.totalAnswered++;
            StudentDAO.INSTANCE.update(currentStudent);
            currentLayerUnansweredCount++;
            currentStudent = null;
        }
        var students = retrieve();
        //当前层数的学生为 0
        if (students.size() == 0) {
            nextLayer();
            return roll();
        }
        //当前层数的学生总数小于指定的未答上来的最大值
        if (students.size() < config.transferCount.get(currentLayer)) {
            if (currentLayerUnansweredCount == students.size()) {
                nextLayer();
                return roll();
            }
            //当前层数未答上来的已经达到上限
        } else if (currentLayerUnansweredCount == config.transferCount.get(currentLayer)) {
            nextLayer();
            return roll();
        }
        var result = students.get(random.nextInt(students.size() - 1));
        if (haveRolled.contains(result)) {
            return roll();
        } else {
            currentStudent = result;
            return result;
        }
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public void answerWrong() throws SQLException {
        if (currentStudent != null) {
            haveRolled.add(currentStudent);
            currentStudent.totalAnswered++;
            currentLayerUnansweredCount++;
            StudentDAO.INSTANCE.update(currentStudent);
            currentStudent = null;
        } else {
            throw new UnsupportedOperationException("Please roll first!");
        }
    }

    private void nextLayer() {
        currentLayerUnansweredCount = 0;
        currentLayer++;
        if (currentLayer > config.layer) {
            reset();
        }
    }

    private void reset() {
        currentLayerUnansweredCount = 0;
        currentLayer = 1;
    }

    private ArrayList<StudentData> retrieve() throws SQLException {
        var leftRatioBound = config.ratio.get(currentLayer - 1);
        return StudentDAO.INSTANCE.retrieve(filter -> {
            //你妈，直接SQL注入！
            filter.head(Condition.greaterEq("rightAnswered`/`totalAnswered", leftRatioBound));
            if (!config.rollAlsoFromNextLayer) {
                double rightRatioBound = 1.0;
                try {
                    rightRatioBound = config.ratio.get(currentLayer);
                } catch (IndexOutOfBoundsException ignored) {
                }
                filter.and(Condition.lessEq("rightAnswered`/`totalAnswered", rightRatioBound));
            }
            filter.or(Condition.eq("totalAnswered", 0));
            return filter;
        });
    }

    public static class Config {
        //分层层数，共分几层。
        public int layer;
        //层数指数，根据回答正确率分层。
        //[0, 0.1, 0.3, 0.7, 0.8]
        public List<Double> ratio;
        //转层定数，当这一层 x 个学生未回答下来问题时转到上一层。
        //[5, 4, 3, 2, 1]
        public List<Integer> transferCount;
        //抽取方式，当处于某层时候，是否也从当前层数的一下一层抽取
        public boolean rollAlsoFromNextLayer;

        public Config(
                int layer,
                List<Double> ratio,
                List<Integer> transferCount,
                boolean rollAlsoFromNextLayer
        ) {
            this.layer = layer;
            if (ratio.size() != layer) {
                throw new IllegalArgumentException("Ratio array's count is not equal to layer");
            }
            this.ratio = ratio;
            if (transferCount.size() != layer) {
                throw new IllegalArgumentException("Ratio array's count is not equal to layer");
            }
            this.transferCount = transferCount;
            this.rollAlsoFromNextLayer = rollAlsoFromNextLayer;
        }
    }
}

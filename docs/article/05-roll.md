### Backend: 基于状态点名实现

#### 原理

每个问题将开启**`一个抽取周期`**。

抽取前，将学生按照先前回答问题的正确率分为 x 层。

现在分为五层(`x = 5`)：

0-0.4 | 0.4-0.6 | 0.6-0.75 | 0.75-0.8 | 0.8-1.0

每一层需要设置一个抽取阈值，当该层 n 个学生回答错误，将转到下一层，下次抽取将从下一层抽取。

现在设置抽取阈值：

4 | 4 | 3 | 3 | 2

抽取时，将先从第一层(正确率 `0 到 0.4` 区间)随机抽取一名学生，并将该学生放进 `已抽取过的学生` 列表中，在下个问题前不会再抽取这名学生。

如果该学生回答正确，**`这个抽取周期就结束了`**，将该学生的回答情况记录下来(总回答数加一)。到下一个问题时将开启新的抽取过程。

如果回答错误，**`这个抽取周期则不会结束`**，将该学生的回答情况记录下来(总回答数和回答正确数加一)，当前层数回答错误人数加一。

继续抽取其他学生，直到有学生回答正确才结束这个抽取周期。

流程图：

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBzdGFydChcIuW8gOWni-aKveWPluWRqOacnzxicj5sYXllciA9IDE8YnI-dW5hbnN3ZXJlZCA9IDBcIilcbiAgICByb2xsKFwi5LuO56ysIGxheWVyIOWxguaKveWPllwiKVxuICAgIHVuYW5zd2VyZWRFcXtcInVuYW5zd2VyZWQ8YnI-5piv5ZCm6LaF6L-H6ZiI5YC8XCJ9XG4gICAgb3ZlcnBhc3MoXCJsYXllciArKzxicj51bmFuc3dlcmVkID0gMFwiKVxuICAgIGlzUmlnaHR7XCLmmK_lkKbmraPnoa5cIn1cbiAgICByaWdodChcIue7k-adn-aKveWPluWRqOacn1wiKVxuICAgIHdyb25nKFwidW5hbnN3ZXJlZCArK1wiKVxuXG4gICAgc3RhcnQgLS0-IHVuYW5zd2VyZWRFcVxuICAgIHVuYW5zd2VyZWRFcSAtLT4gfOacqui2hei_h3xyb2xsXG4gICAgdW5hbnN3ZXJlZEVxIC0tPiB86LaF6L-HfG92ZXJwYXNzXG4gICAgb3ZlcnBhc3MgLS0-IHJvbGxcbiAgICByb2xsIC0tPiBpc1JpZ2h0XG4gICAgaXNSaWdodCAtLT4gfOato-ehrnxyaWdodFxuICAgIGlzUmlnaHQgLS0-IHzplJnor698d3JvbmdcbiAgICB3cm9uZyAtLT4gdW5hbnN3ZXJlZEVxIiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQifSwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ)](https://mermaid-js.github.io/mermaid-live-editor/edit/##eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBzdGFydChcIuW8gOWni-aKveWPluWRqOacnzxicj5sYXllciA9IDE8YnI-dW5hbnN3ZXJlZCA9IDBcIilcbiAgICByb2xsKFwi5LuO56ysIGxheWVyIOWxguaKveWPllwiKVxuICAgIHVuYW5zd2VyZWRFcXtcInVuYW5zd2VyZWQ8YnI-5piv5ZCm6LaF6L-H6ZiI5YC8XCJ9XG4gICAgb3ZlcnBhc3MoXCJsYXllciArKzxicj51bmFuc3dlcmVkID0gXCIpXG4gICAgaXNSaWdodHtcIuaYr-WQpuato-ehrlwifVxuICAgIHJpZ2h0KFwi57uT5p2f5oq95Y-W5ZGo5pyfXCIpXG4gICAgd3JvbmcoXCJ1bmFuc3dlcmVkICsrXCIpXG5cbiAgICBzdGFydCAtLT4gdW5hbnN3ZXJlZEVxXG4gICAgdW5hbnN3ZXJlZEVxIC0tPiB85pyq6LaF6L-HfHJvbGxcbiAgICB1bmFuc3dlcmVkRXEgLS0-IHzotoXov4d8b3ZlcnBhc3NcbiAgICBvdmVycGFzcyAtLT4gcm9sbFxuICAgIHJvbGwgLS0-IGlzUmlnaHRcbiAgICBpc1JpZ2h0IC0tPiB85q2j56GufHJpZ2h0XG4gICAgaXNSaWdodCAtLT4gfOmUmeivr3x3cm9uZ1xuICAgIHdyb25nIC0tPiB1bmFuc3dlcmVkRXEiLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ)

图中 `unanswered` 代表当前层数回答错误的数目， `layer` 代表当前层数。

#### 代码实现：

```java
public class RollSession {
    private final Config config;
    private final Random random;
    private final ArrayList<StudentData> haveRolled;
    private int currentLayer;
    private int currentLayerUnansweredCount;
    private StudentData currentStudent = null;
    private final int totalCount;

    public RollSession(Config config) throws SQLException {
        this.config = config;
        currentLayer = 1;
        currentLayerUnansweredCount = 0;
        random = new Random();
        haveRolled = new ArrayList<>();
        totalCount = StudentDAO.INSTANCE.retrieve(filter -> filter).size();
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
        if (students.size() < config.transferCount.get(currentLayer - 1)) {
            if (currentLayerUnansweredCount == students.size()) {
                nextLayer();
                return roll();
            }
            //当前层数未答上来的已经达到上限
        } else if (currentLayerUnansweredCount == config.transferCount.get(currentLayer - 1)) {
            nextLayer();
            return roll();
        } else if (currentLayerUnansweredCount == totalCount) {
            reset();
            return roll();
        }
        var result = students.get(random.nextInt(students.size()));
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
        haveRolled.clear();
    }

    private ArrayList<StudentData> retrieve() throws SQLException {
        var leftRatioBound = config.ratio.get(currentLayer - 1);
        return StudentDAO.INSTANCE.retrieve(filter -> {
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
}
```

一个 `RollSession` 就代表一个抽取过程。

`roll()` 过程即为具体实现，同时还考虑了一些极端情况和特殊情况的处理。

回答正确或错误时调用 `answerRight()` 和 `answerWrong()` 来进行下一步判断。

#### Session 管理

面向前端时，采用 Session 会话模式管理 `RollSession`

```java
public class SessionManager {
    public static final SessionManager INSTANCE = new SessionManager();
    @SuppressWarnings("SpellCheckingInspection")
    private static final char[] MAPPING = "AB1CD2EF3GH4IJ5KL6MN7OP8QR9ST0UVWXYZ".toCharArray();

    private final Random random;
    private final HashMap<String, RollSession> sessions;

    private RollSession.Config defaultConfig;

    private SessionManager() {
        random = new Random();
        sessions = new HashMap<>();
        defaultConfig = new RollSession.Config(
                4, Arrays.asList(0.0, 0.2, 0.5, 0.7), Arrays.asList(5, 4, 3, 3), false
        );
    }

    public String createNewSession() throws SQLException {
        var sessionKey = generateSessionKey(8);
        if (sessions.containsKey(sessionKey)) {
            createNewSession();
        }
        sessions.put(sessionKey, new RollSession(defaultConfig));
        return sessionKey;
    }

    public void setDefaultConfig(RollSession.Config config) {
        this.defaultConfig = config;
    }

    public void deleteSession(String sessionKey) {
        sessions.remove(sessionKey);
    }

    public RollSession getSession(String sessionKey) {
        return sessions.getOrDefault(sessionKey, null);
    }

    public String generateSessionKey(int bit) {
        var key = new StringBuilder();
        for (var i = 0; i < bit; i++) {
            key.append(MAPPING[random.nextInt(MAPPING.length - 1)]);
        }
        return key.toString();
    }
}
```

前端请求创建 `RollSession`，`SessionManager` 会负责创建并返回一个随机会话密钥绑定到这个 RollSession 上，当其前端需要执行 `roll` 和 `answer`操作时就需要带上密钥来指定操作哪个会话。

> 返回主页：[OOPJavaCurriculumDesign](../index.md)

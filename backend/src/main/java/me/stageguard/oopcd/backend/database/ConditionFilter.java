/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.database;

@SuppressWarnings("unused")
public class ConditionFilter {
    private final StringBuilder statement;
    private boolean isHeadCalled = false;

    public ConditionFilter() {
        statement = new StringBuilder();
    }

    public ConditionFilter head(Condition condition) {
        if(isHeadCalled) {
            throw new UnsupportedOperationException("Method head has already called once.");
        }
        isHeadCalled = true;
        statement.append(condition);
        return this;
    }

    public ConditionFilter and(Condition condition) {
        if(!isHeadCalled) {
            throw new UnsupportedOperationException("Method head hasn't called before calling and.");
        }
        statement.append(" AND ");
        statement.append(condition);
        return this;
    }

    public ConditionFilter or(Condition condition) {
        if(!isHeadCalled) {
            throw new UnsupportedOperationException("Method head hasn't called before calling or.");
        }
        statement.append(" or ");
        statement.append(condition);
        return this;
    }

    @Override
    public String toString() {
        return statement.toString();
    }

    public boolean isEmpty() {
        return statement.toString().isEmpty();
    }

    public static class Condition {
        private final String condition;

        private Condition(String left, Object right, Object op) {
            var sb = new StringBuilder();
            sb.append("`").append(left).append("`");
            sb.append(op);
            if(right instanceof String) {
                sb.append("'").append(right).append("'");
            } else {
                sb.append(right);
            }
            condition = sb.toString();
        }

        @Override
        public String toString() {
            return condition;
        }

        public static Condition eq(String l, Object r) {
            return new Condition(l, r, "=");
        }

        public static Condition less(String l, Object r) {
            return new Condition(l, r, "<");
        }

        public static Condition lessEq(String l, Object r) {
            return new Condition(l, r, "<=");
        }

        public static Condition greater(String l, Object r) {
            return new Condition(l, r, ">");
        }

        public static Condition greaterEq(String l, Object r) {
            return new Condition(l, r, ">=");
        }

        public static Condition notEq(String l, Object r) {
            return new Condition(l, r, "<>");
        }

        public static Condition isNull(String l) {
            return new Condition(l, NullWrapper.INSTANCE, " IS ");
        }

        private static class NullWrapper {
            public static NullWrapper INSTANCE = new NullWrapper();

            @Override
            public String toString() {
                return "NULL";
            }
        }
    }
}


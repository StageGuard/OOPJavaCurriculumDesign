/*
 *  RollCallSystem Copyright (C) 2021 StageGuard
 *
 *  此源代码的使用受 GNU GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU GPLv3 license that can be found through the following link.
 *
 *  https://github.com/StageGuard/OOPJavaCurriculumDesign/blob/main/LICENSE
 */

package me.stageguard.oopcd.backend.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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


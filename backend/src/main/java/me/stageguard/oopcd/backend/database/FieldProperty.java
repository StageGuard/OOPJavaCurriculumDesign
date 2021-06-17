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

    class FieldOrder {
        public final static int DEFAULT = 0;
        public final static int FIRST = 1;
        public final static int AFTER = 2;

    }
}


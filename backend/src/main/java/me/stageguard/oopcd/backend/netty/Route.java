package me.stageguard.oopcd.backend.netty;

import io.netty.handler.codec.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {
    String method() default RouteType.GET;
    String path() default "/";
}
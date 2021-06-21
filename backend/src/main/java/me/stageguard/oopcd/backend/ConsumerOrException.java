package me.stageguard.oopcd.backend;

public interface ConsumerOrException<T, E extends Throwable> {
    void accept(T t) throws E;
}

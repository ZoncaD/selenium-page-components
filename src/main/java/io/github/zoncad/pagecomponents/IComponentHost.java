package io.github.zoncad.pagecomponents;

public interface IComponentHost<T extends IComponentHost<T>> {
    T getHost();
}

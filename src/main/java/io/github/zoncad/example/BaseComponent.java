package io.github.zoncad.example;

import io.github.zoncad.pagecomponents.IComponentHost;
import io.github.zoncad.pagecomponents.RefreshingWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseComponent<T extends IComponentHost<T>> extends Base {
    protected T parent;
    protected RefreshingWebElement root;

    public BaseComponent(WebDriver driver, T parent, By rootLocator, Integer index) {
        super(driver);

        RefreshingWebElement parentElement = null;
        if (parent instanceof BaseComponent<?>) {
            parentElement = ((BaseComponent<?>) parent).root;
        }
        this.root = RefreshingWebElement.of(driver, parentElement, rootLocator, index);
    }

    public BaseComponent(WebDriver driver, T parent, By rootLocator) {
        this(driver, parent, rootLocator, null);
    }
}

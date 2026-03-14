package io.github.zoncad.example;

import io.github.zoncad.pagecomponents.RefreshingWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseComponent<T> extends Base {
    protected T parent;
    protected RefreshingWebElement root;

    public BaseComponent(WebDriver driver, T parent, By rootLocator, Integer index) {
        super(driver);
        this.parent = parent;

        if (parent instanceof BaseComponent<?>) {
            RefreshingWebElement parentElement = ((BaseComponent<?>) parent).root;
            this.root = RefreshingWebElement.locatedBy(parentElement, rootLocator, index);
        }
        else {
            this.root = RefreshingWebElement.locatedBy(driver, rootLocator, index);
        }
    }

    public BaseComponent(WebDriver driver, T parent, By rootLocator) {
        this(driver, parent, rootLocator, null);
    }
}

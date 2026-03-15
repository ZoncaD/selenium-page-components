package io.github.zoncad.pagecomponents;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Base class for page components.
 * @param <T> type of component's parent
 */
@NullMarked
public abstract class BaseComponent<T> extends Base {
    protected T parent;
    protected RefreshingWebElement root;

    public BaseComponent(WebDriver driver, T parent, By rootLocator, @Nullable Integer index) {
        super(driver);
        this.parent = parent;

        if (parent instanceof BaseComponent<?>) {
            RefreshingWebElement parentElement = ((BaseComponent<?>) parent).root;
            this.root = RefreshingWebElement.lazilyLocatedBy(parentElement, rootLocator, index);
        }
        else {
            this.root = RefreshingWebElement.lazilyLocatedBy(driver, rootLocator, index);
        }
    }

    public BaseComponent(WebDriver driver, T parent, By rootLocator) {
        this(driver, parent, rootLocator, null);
    }
}

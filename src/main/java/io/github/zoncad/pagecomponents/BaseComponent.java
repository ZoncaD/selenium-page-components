package io.github.zoncad.pagecomponents;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Base class for page components.
 * @param <T> type of component's parent
 */
@NullMarked
public abstract class BaseComponent<T extends Loadable> extends BaseLoadable {
    protected T parent;
    protected RefreshingWebElement root;

    /**
     * Creates an index-based component
     * @param driver WebDriver instance
     * @param parent parent of this component
     * @param listLocator locator for a desired list elements, within the search context of the parent.
     * @param index index of the desired element within the list of elements found with the listLocator
     */
    public BaseComponent(WebDriver driver, T parent, By listLocator, int index) {
        this(driver, parent, listLocator, Integer.valueOf(index));
    }

    /**
     * Creates a component located by rootLocator.
     * @param driver WebDriver instance
     * @param parent parent of this component
     * @param rootLocator locator for this component's root element, within the search context of the parent.
     */
    public BaseComponent(WebDriver driver, T parent, By rootLocator) {
        this(driver, parent, rootLocator, null);
    }

    /**
     * Creates a component that reuses its parent as its root. Mainly for grouping elements.
     * @param driver WebDriver instance
     * @param parent parent of this component
     */
    public BaseComponent(WebDriver driver, T parent) {
        this(driver, parent, null, null);
    }

    private BaseComponent(WebDriver driver, T parent, @Nullable By rootLocator, @Nullable Integer index) {
        super(driver);
        this.parent = parent;

        if (parent instanceof BaseComponent<?>) {
            rootLocator = rootLocator == null ? By.xpath(".") : rootLocator;
            RefreshingWebElement parentElement = ((BaseComponent<?>) parent).root;
            this.root = RefreshingWebElement.lazilyLocatedBy(parentElement, rootLocator, index);
        }
        else {
            rootLocator = rootLocator == null ? By.xpath("/*") : rootLocator;
            this.root = RefreshingWebElement.lazilyLocatedBy(driver, rootLocator, index);
        }
    }

    @Override
    public ExpectedCondition<Boolean> isLoaded() {
        return ExpectedConditions.not(ExpectedConditions.stalenessOf(root));
    }
}

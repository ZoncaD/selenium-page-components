package io.github.zoncad.pagecomponents;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

public abstract class Base {
    protected final WebDriver driver;
    protected final Wait<WebDriver> wait;

    public Base(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    protected WebElement waitForPresenceOfElementLocated(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * An example compound condition for a RefreshingWebElement that should use the same WebElement instance for all sub conditions
     * @param wrapper the RefreshingWebElement wrapping the WebElement needing evaluation
     */
    protected void waitForSomeCompoundConditionOf(RefreshingWebElement wrapper) {
        wait.until(same(wrapper, unwrapped -> ExpectedConditions.and(
                ExpectedConditions.visibilityOf(unwrapped),
                ExpectedConditions.attributeToBe(unwrapped, "aria-sort", "ascending")
        )));
    }

    /**
     * Expected condition used to ensure compound conditions use the same WebElement instance for each check
     * @param instance wrapper for the WebElement instance to be evaluated
     * @param meetsCondition function that returns the ExpectedConditions to be checked against a given WebElement
     * @return the result of the expected condition as evaluated against the WebElement wrapped inside the
     * RefreshingWebElement instance
     * @param <R> return type of the ExpectedCondition
     */
    protected <R> ExpectedCondition<R> same(RefreshingWebElement instance, Function<WebElement, ExpectedCondition<R>> meetsCondition) {
        return ExpectedConditions.refreshed(driver -> meetsCondition.apply(instance.getWrappedElement()).apply(driver));
    }
}

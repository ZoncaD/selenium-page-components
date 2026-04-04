package io.github.zoncad.pagecomponents;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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

    static <R extends Loadable> ExpectedCondition<R> isLoaded(R loadable) {
        return new ExpectedCondition<>() {
            public R apply(WebDriver driver) {
                try {
                    Boolean loaded = loadable.isLoaded().apply(driver);

                    return Boolean.TRUE.equals(loaded) ? loadable : null;
                } catch (NoSuchElementException | StaleElementReferenceException ignore) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "loadable to be loaded based on condition: " + loadable.isLoaded().toString();
            }
        };
    }

    public <R extends Loadable> R waitUntilLoaded(R loadable) {
        return wait.until(isLoaded(loadable));
    }

    public <R extends Loadable> R waitUntilLoaded(R loadable, Duration timeout) {
        Wait<WebDriver> customWait = new WebDriverWait(driver, timeout);
        return customWait.until(isLoaded(loadable));
    }
}

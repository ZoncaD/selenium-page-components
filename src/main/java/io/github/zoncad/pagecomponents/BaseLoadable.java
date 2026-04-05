package io.github.zoncad.pagecomponents;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseLoadable extends Base implements Loadable {

    public BaseLoadable(WebDriver driver) {
        super(driver);
    }

    @Override
    public BaseLoadable load() {
        return wait.until(isLoaded(this));
    }

    public BaseLoadable load(Duration timeout) {
        Wait<WebDriver> customWait = new WebDriverWait(driver, timeout);
        return customWait.until(isLoaded(this));
    }

    protected static <R extends Loadable> ExpectedCondition<R> isLoaded(R loadable) {
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
}

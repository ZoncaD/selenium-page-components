package io.github.zoncad.pagecomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
}

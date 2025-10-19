import mock.selenium.By;
import mock.selenium.ExpectedCondition;
import mock.selenium.WebDriver;
import mock.selenium.WebElement;

public abstract class BaseActions {
    WebDriver driver;

    public BaseActions(WebDriver driver) {
        this.driver = driver;
    }

    protected WebElement waitForPresenceOfElementLocated(By locator) {
        return new WebElement();
    }

    protected WebElement waitForConditionOfElementLocatedBy(ExpectedCondition condition, By rootLocator, By... childLocators) {
        return new WebElement();
    }
}

import mock.selenium.WebDriver;
import mock.selenium.WebElement;

public abstract class BaseComponent extends BaseActions {
    protected WebDriver driver;
    protected WebElement root;

    public BaseComponent(WebDriver driver, WebElement root) {
        super(driver);
        this.driver = driver;
        this.root = root;
    }
}

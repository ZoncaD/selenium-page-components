import mock.selenium.WebDriver;

public abstract class BasePage extends BaseActions {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseComponent extends BaseActions {
    protected WebDriver driver;
    protected By rootLocator;

    public BaseComponent(WebDriver driver, By rootLocator) {
        super(driver);
        this.driver = driver;
        this.rootLocator = rootLocator;
    }
}

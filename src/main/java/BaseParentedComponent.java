import mock.selenium.WebDriver;
import mock.selenium.WebElement;

public abstract class BaseParentedComponent<T extends IComponentHost<T>> extends BaseComponent {
    protected T parent;

    public BaseParentedComponent(WebDriver driver, T parent, WebElement root) {
        super(driver, root);
        this.parent = parent;
    }
}

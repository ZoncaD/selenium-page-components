import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseParentedComponent<T extends IComponentHost<T>> extends BaseComponent {
    protected T parent;

    public BaseParentedComponent(WebDriver driver, T parent, By rootLocator) {
        super(driver, rootLocator);
        this.parent = parent;
    }
}

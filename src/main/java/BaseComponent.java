import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseComponent<T extends IComponentHost<T>> extends BaseActions {
    protected T parent;
    protected LazyWebElement root;

    public BaseComponent(WebDriver driver, T parent, By rootLocator, Integer index) {
        super(driver);

        LazyWebElement parentElement = null;
        if (parent instanceof BaseComponent<?>) {
            parentElement = ((BaseComponent<?>) parent).root;
        }
        this.root = new LazyWebElement(driver, parentElement, rootLocator, index);
    }

    public BaseComponent(WebDriver driver, T parent, By rootLocator) {
        this(driver, parent, rootLocator, null);
    }
}

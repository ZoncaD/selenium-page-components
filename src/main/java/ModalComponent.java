import mock.selenium.WebDriver;
import mock.selenium.WebElement;

public class ModalComponent<T extends IComponentHost<T>> extends BaseParentedComponent<T> implements ISimpleComponentHost<ModalComponent<T>> {
    public ModalComponent(WebDriver driver, T parent, WebElement root) {
        super(driver, parent, root);
    }

    @Override
    public ModalComponent<T> getHost() {
        return this;
    }

    public ModalComponent<T> performModalAction() {
        return this;
    }

    public T close() {
        return parent;
    }

    @Override
    public SimpleComponent getSimpleComponent() {
        return new SimpleComponent(driver, new WebElement());
    }
}

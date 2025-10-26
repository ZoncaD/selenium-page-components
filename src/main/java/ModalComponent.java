import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ModalComponent<T extends IComponentHost<T>> extends BaseParentedComponent<T> implements ISimpleComponentHost<ModalComponent<T>> {
    public ModalComponent(WebDriver driver, T parent, By rootLocator) {
        super(driver, parent, rootLocator);
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
        return new SimpleComponent(driver, By.xpath("//*"));
    }
}

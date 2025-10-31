import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ModalComponent<T extends IComponentHost<T>> extends BaseComponent<T> implements ISimpleComponentHost<ModalComponent<T>> {
    private final SimpleComponent<ModalComponent<T>> simpleComponent;

    public ModalComponent(WebDriver driver, T parent, By rootLocator) {
        super(driver, parent, rootLocator);
        simpleComponent = new SimpleComponent<>(driver, this, By.xpath("//*"));
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

    /*   x Methods   */

    @Override
    public ModalComponent<T> performSimpleComponentAction() {
        return null;
    }

    @Override
    public String getSimpleComponentValue() {
        return "";
    }
}

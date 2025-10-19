import mock.selenium.WebDriver;
import mock.selenium.WebElement;

public class ModalSpawningComponent<T extends IComponentHost<T>> extends BaseParentedComponent<T> {

    public ModalSpawningComponent(WebDriver driver, WebElement root, T parent) {
        super(driver, parent, root);
    }

    public ModalSpawningComponent<T> performModalSpawningComponentAction() {
        return this;
    }

    public ModalComponent<T> openModal() {
        return new ModalComponent<>(driver, parent, new WebElement());
    }
}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ModalSpawningComponent<T extends IComponentHost<T>> extends BaseParentedComponent<T> {

    public ModalSpawningComponent(WebDriver driver, By rootLocator, T parent) {
        super(driver, parent, rootLocator);
    }

    public ModalSpawningComponent<T> performModalSpawningComponentAction() {
        return this;
    }

    public ModalComponent<T> openModal() {
        return new ModalComponent<>(driver, parent, By.xpath("//*"));
    }
}

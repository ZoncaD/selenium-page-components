import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ModalSpawningComponent<T extends IComponentHost<T>> extends BaseComponent<T> {
    private final ModalComponent<T> modalComponent;

    public ModalSpawningComponent(WebDriver driver, T parent, By rootLocator) {
        super(driver, parent, rootLocator);
        modalComponent = new ModalComponent<>(driver, parent, By.xpath("//*"));
    }

    public ModalSpawningComponent<T> performModalSpawningComponentAction() {
        return this;
    }

    public ModalComponent<T> openModal() {
        return modalComponent;
    }
}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SomePage extends BasePage implements IComponentHost<SomePage> {
    private final ModalSpawningComponent<SomePage> modalSpawningComponent;
    private final ParentComponent<SomePage> parentComponent;

    public SomePage(WebDriver driver) {
        super(driver);
        modalSpawningComponent = new ModalSpawningComponent<>(driver, this, By.xpath("//*"));
        parentComponent = new ParentComponent<>(driver, this, By.xpath("//*"));
    }

    @Override
    public SomePage getHost() {
        return this;
    }

    public ModalSpawningComponent<SomePage> getModalSpawningComponent() {
        return modalSpawningComponent;
    }

    public ParentComponent<SomePage> getParentComponent() {
        return parentComponent;
    }

    public int getSomePageNumber(int num) {
        return num * 2;
    }

    public SomePage performSomePageAction() {
        return this;
    }

    public SomePage performModalSpawningComponentAction() {
        return null;
    }

    public ModalComponent<SomePage> openModal() {
        return null;
    }

    public SomePage performParentComponentAction() {
        return null;
    }

    public boolean isParentComponentSomething() {
        return false;
    }

    public SomePage performSimpleComponentAction() {
        return null;
    }

    public String getSimpleComponentValue() {
        return "";
    }
}

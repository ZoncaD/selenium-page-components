import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SomePage extends BasePage implements IModalSpawningComponentHost<SomePage>, IParentComponentHost<SomePage> {
    private ModalSpawningComponent<SomePage> modalSpawningComponent;
    private ParentComponent parentComponent;

    public SomePage(WebDriver driver) {
        super(driver);
        modalSpawningComponent = new ModalSpawningComponent<>(driver, By.xpath("//*"), this);
        parentComponent = new ParentComponent(driver, By.xpath("//*"));
    }

    @Override
    public SomePage getHost() {
        return this;
    }

    @Override
    public ModalSpawningComponent<SomePage> getModalSpawningComponent() {
        return modalSpawningComponent;
    }

    @Override
    public ParentComponent getParentComponent() {
        return parentComponent;
    }

    public int getSomePageNumber(int num) {
        return num * 2;
    }

    public SomePage performSomePageAction() {
        return this;
    }
}

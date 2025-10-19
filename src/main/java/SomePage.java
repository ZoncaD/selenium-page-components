import mock.selenium.WebDriver;
import mock.selenium.WebElement;

public class SomePage extends BasePage implements IModalSpawningComponentHost<SomePage>, IParentComponentHost<SomePage> {

    public SomePage(WebDriver driver) {
        super(driver);
    }

    public SomePage() {
        this(new WebDriver());
    }

    @Override
    public SomePage getHost() {
        return this;
    }

    @Override
    public ModalSpawningComponent<SomePage> getModalSpawningComponent() {
        return new ModalSpawningComponent<>(driver, new WebElement(), this);
    }

    @Override
    public ParentComponent getParentComponent() {
        return new ParentComponent(driver, new WebElement());
    }

    public int getSomePageNumber(int num) {
        return num * 2;
    }

    public SomePage performSomePageAction() {
        return this;
    }

}

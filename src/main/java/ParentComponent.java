import mock.selenium.WebDriver;
import mock.selenium.WebElement;

public class ParentComponent extends BaseComponent implements ISimpleComponentHost<ParentComponent>{
    public ParentComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    @Override
    public ParentComponent getHost() {
        return this;
    }

    @Override
    public SimpleComponent getSimpleComponent() {
        return new SimpleComponent(driver, new WebElement());
    }

    public ParentComponent performParentComponentAction() {
        return this;
    }

    public boolean isParentComponentSomething() {
        return true;
    }
}

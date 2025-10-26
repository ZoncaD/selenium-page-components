import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ParentComponent extends BaseComponent implements ISimpleComponentHost<ParentComponent>{
    public ParentComponent(WebDriver driver, By rootLocator) {
        super(driver, rootLocator);
    }

    @Override
    public ParentComponent getHost() {
        return this;
    }

    @Override
    public SimpleComponent getSimpleComponent() {
        return new SimpleComponent(driver, By.xpath("//*"));
    }

    public ParentComponent performParentComponentAction() {
        return this;
    }

    public boolean isParentComponentSomething() {
        return true;
    }
}

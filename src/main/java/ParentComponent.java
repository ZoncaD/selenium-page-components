import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class ParentComponent<T extends IComponentHost<T>> extends BaseComponent<T> implements IComponentHost<ParentComponent<T>> {
    private final By SIMPLE_COMPONENT_LOCATOR = By.xpath(".//*");

    private final SimpleComponent<ParentComponent<T>> simpleComponent;

    public ParentComponent(WebDriver driver, T parent, By rootLocator) {
        super(driver, parent, rootLocator);
        simpleComponent = new SimpleComponent<>(driver, this, SIMPLE_COMPONENT_LOCATOR);
    }

    @Override
    public ParentComponent<T> getHost() {
        return this;
    }

    public ParentComponent<T> performParentComponentAction() {
        return this;
    }

    public boolean isParentComponentSomething() {
        return true;
    }

    public ParentComponent<T> performSimpleComponentAction() {
        return simpleComponent.performSimpleComponentAction();
    }

    public String getSimpleComponentValue() {
        return "";
    }
}

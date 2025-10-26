import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SimpleComponent extends BaseComponent {
    public SimpleComponent(WebDriver driver, By rootLocator) {
        super(driver, rootLocator);
    }

    public SimpleComponent performSimpleComponentAction() {
        return this;
    }

    public String getSimpleComponentValue() {
        return "Simple Page Component";
    }
}

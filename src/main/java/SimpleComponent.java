import mock.selenium.WebDriver;
import mock.selenium.WebElement;

public class SimpleComponent extends BaseComponent {
    public SimpleComponent(WebDriver driver, WebElement root) {
        super(driver, root);
    }

    public SimpleComponent performSimpleComponentAction() {
        return this;
    }

    public String getSimpleComponentValue() {
        return "Simple Page Component";
    }
}

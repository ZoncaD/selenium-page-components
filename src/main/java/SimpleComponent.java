import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SimpleComponent<T extends IComponentHost<T>> extends BaseComponent<T> {
    public SimpleComponent(WebDriver driver, T parent, By rootLocator) {
        super(driver, parent, rootLocator);
    }

    public T performSimpleComponentAction() {
        root.findElement(By.xpath(".//*")).click();
        return parent;
    }

    public String getSimpleComponentValue() {
        return "Simple Page Component";
    }
}

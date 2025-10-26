import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SomeTest {
    WebDriver driver;

    @BeforeTest
    public void testSetup() {
        driver = new ChromeDriver();
    }

    @AfterTest
    public void testCleanup() {
        driver.close();
    }

    @Test
    public void fakeTest() {
        SomePage page = new SomePage(driver);

        int ignoredNum = page.performSomePageAction().getSomePageNumber(5);
        page.performSomePageAction().openModal();
        page.performSomePageAction().performModalSpawningComponentAction();

        ModalComponent<SomePage> modal = page.performSomePageAction().openModal();
        modal = page.performSomePageAction().openModal().performModalAction();
        String value = modal.performSimpleComponentAction().getSimpleComponentValue();

        page = modal.performModalAction().close();
        page = page.performParentComponentAction().performSimpleComponentAction().performModalSpawningComponentAction();
        boolean isParent = page.isParentComponentSomething();

        Assert.assertTrue(true);
    }
}

package io.github.zoncad.example;

import io.github.zoncad.example.angularmaterial.ComponentsPage;
import io.github.zoncad.example.angularmaterial.LandingPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class AngularMaterialTests {
    WebDriver driver;

    @BeforeMethod
    public void testSetup() {
        driver = new ChromeDriver();
        driver.get("https://v21.material.angular.dev");
        driver.manage().window().fullscreen();
    }

    @AfterMethod
    public void testCleanup() {
        driver.close();
    }

    @Test
    public void changeThemeTest() throws InterruptedException {
        new LandingPage(driver).getHeaderComponent().selectThemeOption("Rose & Red");
        // Assert something . . .
        Thread.sleep(2000);
    }

    @Test
    public void componentsPageSideNavOptionsTest() {
        List<String> actual = new LandingPage(driver).getHeaderComponent().clickComponents().getSideNavComponent().getOptions();
        Assert.assertListContains(actual, "Dialog"::equals, "Side bar to contain 'Dialog' option");
    }

    @Test
    public void cdkPageSideNavOptionsTest() {
        List<String> actual = new LandingPage(driver).getHeaderComponent().clickCdk().getSideNavComponent().getOptions();
        Assert.assertListContains(actual, "Portal"::equals, "Side bar to contain 'Portal' option");
    }

    @Test
    public void categoriesPresentTest() {
        List<ComponentCardInfo> expected = List.of(
                new ComponentCardInfo("Button", "An interactive button with a range of presentation options."),
                new ComponentCardInfo("Input", "Enables native inputs to be used within a Form field."),
                new ComponentCardInfo("Table", "A configurable component for displaying tabular data.")
        );

        ComponentsPage page = new LandingPage(driver).getHeaderComponent().clickComponents();
        List<ComponentCardInfo> actual = page.getComponentCardsWithRefreshingWebElements().stream()
                .map(item -> new ComponentCardInfo(item.getTitle(), item.getSummary()))
                .toList();

        Assert.assertTrue(actual.containsAll(expected), "Select categories present");
    }

    @Test
    public void categoriesPresentRegularWebElementTest() {
        List<ComponentCardInfo> expected = List.of(
                new ComponentCardInfo("Button", "An interactive button with a range of presentation options."),
                new ComponentCardInfo("Input", "Enables native inputs to be used within a Form field."),
                new ComponentCardInfo("Table", "A configurable component for displaying tabular data.")
        );

        ComponentsPage page = new LandingPage(driver).getHeaderComponent().clickComponents();
        List<ComponentCardInfo> actual = page.getComponentCardsWithWebElements().stream()
                .map(item -> new ComponentCardInfo(item.getTitle(), item.getSummary()))
                .toList();

        Assert.assertTrue(actual.containsAll(expected), "Select categories present");
    }

    private record ComponentCardInfo(String title, String summary) { }
}

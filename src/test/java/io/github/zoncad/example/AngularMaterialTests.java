package io.github.zoncad.example;

import io.github.zoncad.example.angularmaterial.ComponentsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class AngularMaterialTests {
    WebDriver driver;

    @BeforeTest
    public void testSetup() {
        driver = new ChromeDriver();
        driver.get("https://v21.material.angular.dev/components/categories");
    }

    @AfterTest
    public void testCleanup() {
        driver.close();
    }

    @Test
    public void categoriesPresentTest() {
        List<ComponentCardInfo> expected = List.of(
                new ComponentCardInfo("Button", "An interactive button with a range of presentation options."),
                new ComponentCardInfo("Input", "Enables native inputs to be used within a Form field."),
                new ComponentCardInfo("Table", "A configurable component for displaying tabular data.")
        );

        ComponentsPage page = new ComponentsPage(driver);
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

        ComponentsPage page = new ComponentsPage(driver);
        List<ComponentCardInfo> actual = page.getComponentCardsWithWebElements().stream()
                .map(item -> new ComponentCardInfo(item.getTitle(), item.getSummary()))
                .toList();

        Assert.assertTrue(actual.containsAll(expected), "Select categories present");
    }

    private record ComponentCardInfo(String title, String summary) { }
}

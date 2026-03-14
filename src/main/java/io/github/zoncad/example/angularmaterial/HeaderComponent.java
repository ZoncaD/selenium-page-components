package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.example.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HeaderComponent<T> extends BaseComponent<T> {
    private static final By rootLocator = By.tagName("app-navbar");
    private static final String sectionSelector = ".//nav[contains(concat(' ',normalize-space(@class),' '),' docs-navbar-header ')]"
            + "//a[.//span[text()='%s']]";
    public HeaderComponent(WebDriver driver, T parent) {
        super(driver, parent, rootLocator);
    }

    public ComponentsPage clickComponents() {
        root.findElement(By.xpath(String.format(sectionSelector, "Components"))).click();
        return new ComponentsPage(driver);
    }

    public CdkPage clickCdk() {
        root.findElement(By.xpath(String.format(sectionSelector, "CDK"))).click();
        return new CdkPage(driver);
    }

    public T selectThemeOption(String themeName) {
        root.findElement(By.tagName("theme-picker")).click();
        WebElement menu = waitForPresenceOfElementLocated(By.cssSelector("div.docs-theme-picker-menu"));
        menu.findElement(By.xpath(String.format(".//button[@mat-menu-item and .//span[text()='%s']]", themeName))).click();
        return parent;
    }

    /*
     * Other methods
     */
}

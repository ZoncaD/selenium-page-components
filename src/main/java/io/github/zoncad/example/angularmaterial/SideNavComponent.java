package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.pagecomponents.BaseComponent;
import io.github.zoncad.pagecomponents.BaseLoadable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SideNavComponent<T extends BaseLoadable> extends BaseComponent<T> {
    private static final By rootLocator = By.xpath("//app-component-nav//mat-nav-list");
    public SideNavComponent(WebDriver driver, T parent) {
        super(driver, parent, rootLocator);
    }

    public List<String> getOptions() {
        return root.findElements(By.xpath(".//a/span")).stream().map(WebElement::getText).toList();
    }
}

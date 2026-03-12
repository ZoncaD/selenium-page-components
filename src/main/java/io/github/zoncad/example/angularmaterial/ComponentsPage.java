package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.example.BasePage;
import io.github.zoncad.pagecomponents.RefreshingWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.github.zoncad.pagecomponents.IComponentHost;

import java.util.List;

public class ComponentsPage extends BasePage implements IComponentHost<ComponentsPage> {

    public ComponentsPage(WebDriver driver) {
        super(driver);
        if (!"Components • Angular Material".equals(driver.getTitle())) {
            throw new IllegalStateException("This is not the Components page. Current URL: " + driver.getCurrentUrl());
        }
    }

    @Override
    public ComponentsPage getHost() {
        return this;
    }

    public List<ComponentCard> getComponentCardsWithRefreshingWebElements() {
        return RefreshingWebElement.ofAll(driver, By.cssSelector("a.docs-component-category-list-item")).stream()
                .map(ComponentCard::new)
                .toList();
    }

    public List<ComponentCard> getComponentCardsWithWebElements() {
        return driver.findElements(By.cssSelector("a.docs-component-category-list-item")).stream()
                .map(ComponentCard::new)
                .toList();
    }
}

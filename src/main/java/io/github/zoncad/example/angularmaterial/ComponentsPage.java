package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.pagecomponents.BaseLoadable;
import io.github.zoncad.pagecomponents.RefreshingWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ComponentsPage extends BaseLoadable {
    private final HeaderComponent<ComponentsPage> headerNav;
    private final SideNavComponent<ComponentsPage> sideNav;

    public ComponentsPage(WebDriver driver) {
        super(driver);
        headerNav = new HeaderComponent<>(driver, this);
        sideNav = new SideNavComponent<>(driver, this);
        load();
    }

    public List<PseudoCard> getComponentCardsWithRefreshingWebElements() {
        return RefreshingWebElement.listLocatedBy(driver, By.cssSelector("a.docs-component-category-list-item")).stream()
                .map(PseudoCard::new)
                .toList();
    }

    public List<PseudoCard> getComponentCardsWithWebElements() {
        return driver.findElements(By.cssSelector("a.docs-component-category-list-item")).stream()
                .map(PseudoCard::new)
                .toList();
    }

    public HeaderComponent<ComponentsPage> getHeaderComponent() {
        return headerNav;
    }

    public SideNavComponent<ComponentsPage> getSideNavComponent() {
        return sideNav;
    }

    @Override
    public ExpectedCondition<Boolean> isLoaded() {
        return ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='category-summary']//div[contains(text(), 'Angular Material offers a wide variety of UI components')]")),
                headerNav.isLoaded(),
                sideNav.isLoaded()
        );
    }
}

package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.pagecomponents.Base;
import io.github.zoncad.pagecomponents.RefreshingWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class ComponentsPage extends Base {
    private final HeaderComponent<ComponentsPage> headerNav;
    private final SideNavComponent<ComponentsPage> sideNav;

    public ComponentsPage(WebDriver driver) {
        super(driver);
        waitForPresenceOfElementLocated(By.xpath("//*[@id='category-summary']//div[contains(text(), 'Angular Material offers a wide variety of UI components')]"));
        headerNav = new HeaderComponent<>(driver, this);
        sideNav = new SideNavComponent<>(driver, this);
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
}

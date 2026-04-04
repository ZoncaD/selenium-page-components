package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.pagecomponents.BaseLoadable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CdkPage extends BaseLoadable {
    private final HeaderComponent<CdkPage> headerNav;
    private final SideNavComponent<CdkPage> sideNav;

    public CdkPage(WebDriver driver) {
        super(driver);
        headerNav = new HeaderComponent<>(driver, this);
        sideNav = new SideNavComponent<>(driver, this);
        waitUntilLoaded(this);
    }

    public HeaderComponent<CdkPage> getHeaderComponent() {
        return headerNav;
    }

    public SideNavComponent<CdkPage> getSideNavComponent() {
        return sideNav;
    }

    @Override
    public ExpectedCondition<Boolean> isLoaded() {
        return ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='category-summary']//div[contains(text(), 'The Component Dev Kit (CDK) is a set of behavior primitives for building UI components.')]")),
                headerNav.isLoaded(),
                sideNav.isLoaded()
        );
    }
}

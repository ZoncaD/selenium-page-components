package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.example.BasePage;
import io.github.zoncad.pagecomponents.IComponentHost;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CdkPage extends BasePage implements IComponentHost<CdkPage> {
    private final HeaderComponent<CdkPage> headerNav;
    private final SideNavComponent<CdkPage> sideNav;

    public CdkPage(WebDriver driver) {
        super(driver);
        waitForPresenceOfElementLocated(By.xpath("//*[@id='category-summary']//div[contains(text(), 'The Component Dev Kit (CDK) is a set of behavior primitives for building UI components.')]"));
        headerNav = new HeaderComponent<>(driver, this);
        sideNav = new SideNavComponent<>(driver, this);
    }

    @Override
    public CdkPage getHost() {
        return this;
    }

    public HeaderComponent<CdkPage> getHeaderComponent() {
        return headerNav;
    }

    public SideNavComponent<CdkPage> getSideNavComponent() {
        return sideNav;
    }
}

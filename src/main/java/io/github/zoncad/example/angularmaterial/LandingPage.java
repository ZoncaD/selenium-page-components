package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.example.BasePage;
import io.github.zoncad.pagecomponents.IComponentHost;
import org.openqa.selenium.WebDriver;

public class LandingPage extends BasePage implements IComponentHost<LandingPage> {
    private final HeaderComponent<LandingPage> headerNav;

    public LandingPage(WebDriver driver) {
        super(driver);
        // Some check to confirm correct page is loaded
        if (!"Angular Material UI component library".equals(driver.getTitle())) {
            throw new IllegalStateException("This is not the landing page");
        }
        headerNav = new HeaderComponent<>(driver, this);
    }

    @Override
    public LandingPage getHost() {
        return this;
    }

    public HeaderComponent<LandingPage> getHeaderComponent() {
        return headerNav;
    }
}

package io.github.zoncad.example.angularmaterial;

import io.github.zoncad.pagecomponents.BaseLoadable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LandingPage extends BaseLoadable {
    private final HeaderComponent<LandingPage> headerNav;

    public LandingPage(WebDriver driver) {
        super(driver);
        headerNav = new HeaderComponent<>(driver, this);
        load();
    }

    public HeaderComponent<LandingPage> getHeaderComponent() {
        return headerNav;
    }

    @Override
    public ExpectedCondition<Boolean> isLoaded() {
        return ExpectedConditions.and(
                ExpectedConditions.titleIs("Angular Material UI component library"),
                headerNav.isLoaded()
        );
    }
}

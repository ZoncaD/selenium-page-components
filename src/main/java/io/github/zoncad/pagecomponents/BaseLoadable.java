package io.github.zoncad.pagecomponents;

import org.openqa.selenium.WebDriver;

public abstract class BaseLoadable extends Base implements Loadable {

    public BaseLoadable(WebDriver driver) {
        super(driver);
    }
}

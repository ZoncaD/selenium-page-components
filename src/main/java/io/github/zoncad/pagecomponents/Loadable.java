package io.github.zoncad.pagecomponents;

import org.openqa.selenium.support.ui.ExpectedCondition;

public interface Loadable {
    /**
     * Returns an ExpectedCondition that can be used to determine if the component is loaded.
     * @return ExpectedCondition that determines if component is loaded.
     */
    ExpectedCondition<Boolean> isLoaded();

    /**
     * Loads this Loadable and waits for it to finish loading.
     * @return this loadable in a loaded state
     */
    Loadable load();
}

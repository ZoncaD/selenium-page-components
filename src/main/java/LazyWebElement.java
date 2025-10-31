import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class LazyWebElement implements WebElement {
    private final WebDriver driver;
    private final By locator;
    private final Integer index;
    private final LazyWebElement parent;
    private WebElement instance;

    public LazyWebElement(WebDriver driver, LazyWebElement parent, By locator, Integer index) {
        if (index != null && index < 1) throw new IllegalArgumentException("Argument 'index' must be either null or a positive integer");
        this.driver = driver;
        this.locator = locator;
        this.parent = parent;
        this.index = index;
    }

    public LazyWebElement(WebDriver driver, By locator) {
        this(driver, null, locator, null);
    }

    public LazyWebElement(WebDriver driver, By locator, Integer index) {
        this(driver, null, locator, index);
    }

    public LazyWebElement(WebDriver driver, LazyWebElement parent, By locator) {
        this(driver, parent, locator, null);
    }

    private WebElement getNewInstance() {
        WebElement freshInstance;
        if (parent == null) {
            freshInstance = driver.findElement(locator);
        }
        else  {
            freshInstance = parent.getInstance().findElement(locator);
        }
        return freshInstance;
    }

    private WebElement getNewInstanceWithIndex() {
        List<WebElement> matches;
        if (parent == null) {
            matches = driver.findElements(locator);
        }
        else  {
            matches = parent.getInstance().findElements(locator);
        }

        if (matches.size() < index) {
            throw new NoSuchElementException("No element with index " + index + " found");
        }

        return matches.get(index - 1);
    }

    private WebElement getInstance() {
        if (instance == null || Boolean.TRUE.equals(ExpectedConditions.stalenessOf(instance).apply(driver))) {
            instance = index == null ? getNewInstance() : getNewInstanceWithIndex();
        }

        return instance;
    }

    @Override
    public void click() {
        getInstance().click();
    }

    @Override
    public void submit() {
        getInstance().submit();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        getInstance().sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        getInstance().clear();
    }

    @Override
    public String getTagName() {
        return getInstance().getTagName();
    }

    @Override
    public String getDomProperty(String name) {
        return getInstance().getDomProperty(name);
    }

    @Override
    public String getDomAttribute(String name) {
        return getInstance().getDomAttribute(name);
    }

    @Override
    public @Nullable String getAttribute(String name) {
        return getInstance().getAttribute(name);
    }

    @Override
    public String getAriaRole() {
        return getInstance().getAriaRole();
    }

    @Override
    public String getAccessibleName() {
        return getInstance().getAccessibleName();
    }

    @Override
    public boolean isSelected() {
        return getInstance().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return getInstance().isEnabled();
    }

    @Override
    public String getText() {
        return getInstance().getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return getInstance().findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return getInstance().findElement(by);
    }

    @Override
    public SearchContext getShadowRoot() {
        return getInstance().getShadowRoot();
    }

    @Override
    public boolean isDisplayed() {
        return getInstance().isDisplayed();
    }

    @Override
    public Point getLocation() {
        return getInstance().getLocation();
    }

    @Override
    public Dimension getSize() {
        return getInstance().getSize();
    }

    @Override
    public Rectangle getRect() {
        return getInstance().getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return getInstance().getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return getInstance().getScreenshotAs(target);
    }
}

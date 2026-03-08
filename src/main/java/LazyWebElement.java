import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;
import java.util.stream.IntStream;

public class LazyWebElement implements WebElement {
    private final WebDriver driver;
    private final By locator;
    private final LazyWebElement parent;

    private static final WeakHashMap<WebElementListIdentifier, List<WebElement>> instanceLists = new WeakHashMap<>();
    private final WebElementListIdentifier listIdentifier;
    private final Integer index;

    private WebElement instance;

    public LazyWebElement(WebDriver driver, LazyWebElement parent, By locator, Integer index) {
        if (index != null) {
            if (index < 0) {
                throw new IllegalArgumentException("Argument 'index' must be either null or a non-negative integer");
            }
            else {
                listIdentifier = getListIdentifier(parent, locator);
            }
        }
        else {
            listIdentifier = null;
        }

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

    public List<LazyWebElement> findElementsAsProxies(By by) {
        List<WebElement> matches = getInstance().findElements(by);
        if (!matches.isEmpty()) {
            WebElementListIdentifier listId = getListIdentifier(this, by);
            instanceLists.put(listId, matches);
        }

        return IntStream.range(0, matches.size())
                .mapToObj(i -> new LazyWebElement(driver, this, by, i))
                .toList();
    }

    private WebElementListIdentifier getListIdentifier(LazyWebElement parent, By locator) {
        final WebElementListIdentifier newIdentifier = new WebElementListIdentifier(parent, locator);
        Optional<WebElementListIdentifier> exactKeyObj = instanceLists.keySet().stream()
                .filter(key -> key.equals(newIdentifier))
                .findFirst();
        return exactKeyObj.orElse(newIdentifier);
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

    private WebElement getNewInstanceFromList() {
        List<WebElement> matches = instanceLists.getOrDefault(listIdentifier, new ArrayList<>());
        if (matches.size() <= index || Boolean.TRUE.equals(ExpectedConditions.stalenessOf(matches.get(index)).apply(driver))) {
            if (parent == null) {
                matches = driver.findElements(locator);
            }
            else  {
                matches = parent.getInstance().findElements(locator);
            }
        }

        if (matches.isEmpty()) {
            instanceLists.remove(listIdentifier);
        }
        else {
            instanceLists.put(listIdentifier, matches);
        }

        if (matches.size() <= index) {
            throw new NoSuchElementException("No element with index " + index + " found for locator " + locator);
        }

        return matches.get(index);
    }

    private WebElement getInstance() {
        if (instance == null || Boolean.TRUE.equals(ExpectedConditions.stalenessOf(instance).apply(driver))) {
            instance = listIdentifier == null ? getNewInstance() : getNewInstanceFromList();
        }

        return instance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LazyWebElement that = (LazyWebElement) o;
        return Objects.equals(locator, that.locator) && Objects.equals(parent, that.parent) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locator, parent, index);
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

    private static class WebElementListIdentifier {
        public final LazyWebElement parent;
        public final By locator;

        public WebElementListIdentifier(LazyWebElement parent, By locator) {
            this.parent = parent;
            this.locator = locator;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            WebElementListIdentifier that = (WebElementListIdentifier) o;
            return Objects.equals(parent, that.parent) && Objects.equals(locator, that.locator);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parent, locator);
        }
    }
}

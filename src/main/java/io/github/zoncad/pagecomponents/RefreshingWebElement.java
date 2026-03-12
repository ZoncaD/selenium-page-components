package io.github.zoncad.pagecomponents;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;
import java.util.stream.IntStream;

@NullMarked
public class RefreshingWebElement implements WebElement {
    protected final WebDriver driver;
    @Nullable protected final RefreshingWebElement parent;
    protected final By locator;

    @Nullable protected WebElement instance;

    protected RefreshingWebElement(WebDriver driver, @Nullable RefreshingWebElement parent, By locator) {
        this.driver = driver;
        this.locator = locator;
        this.parent = parent;
    }

    public static RefreshingWebElement of(WebDriver driver, @Nullable RefreshingWebElement parent, By locator) {
        RefreshingWebElement theElement = new RefreshingWebElement(driver, parent, locator);
        theElement.getInstance();
        return theElement;
    }

    public static RefreshingWebElement of(WebDriver driver, @Nullable RefreshingWebElement parent, By locator, @Nullable Integer index) {
        if (index == null) {
            return of(driver, parent, locator);
        }
        else {
            RefreshingWebElement theElement = new IndexedRefreshingWebElement(driver, parent, locator, index);
            theElement.getInstance();
            return theElement;
        }
    }

    public static RefreshingWebElement of(WebDriver driver, By locator) {
        return of(driver, null, locator);
    }

    public static RefreshingWebElement of(WebDriver driver, By locator, @Nullable Integer index) {
        if (index == null) {
            return of(driver, locator);
        }
        else {
            return of(driver, null, locator, index);
        }
    }

    public static List<RefreshingWebElement> ofAll(WebDriver driver, @Nullable RefreshingWebElement parent, By locator) {
        IndexedRefreshingWebElement.WebElementListIdentifier listIdentifier = IndexedRefreshingWebElement.getListIdentifier(parent, locator);
        int numMatches = IndexedRefreshingWebElement.updateListInstances(driver, listIdentifier, parent, locator);

        return IntStream.range(0, numMatches)
                .mapToObj(i -> of(driver, parent, locator, i))
                .toList();
    }

    public static List<RefreshingWebElement> ofAll(WebDriver driver, By locator) {
        return ofAll(driver, null, locator);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RefreshingWebElement that)) return false;
        return Objects.equals(driver, that.driver) && Objects.equals(parent, that.parent) && Objects.equals(locator, that.locator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driver, parent, locator);
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
    @Nullable
    public String getDomProperty(String name) {
        return getInstance().getDomProperty(name);
    }

    @Override
    @Nullable
    public String getDomAttribute(String name) {
        return getInstance().getDomAttribute(name);
    }

    @Override
    @Nullable
    public String getAttribute(String name) {
        return getInstance().getAttribute(name);
    }

    @Override
    @Nullable
    public String getAriaRole() {
        return getInstance().getAriaRole();
    }

    @Override
    @Nullable
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
    public List<WebElement> findElements(By locator) {
        return new ArrayList<>(ofAll(driver, this, locator));
    }

    @Override
    public WebElement findElement(By locator) {
        return of(driver, this, locator);
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

    protected WebElement getFreshInstance() {
        WebElement freshInstance;
        if (parent == null) {
            freshInstance = driver.findElement(locator);
        }
        else  {
            freshInstance = parent.getInstance().findElement(locator);
        }

        return freshInstance;
    }

    private WebElement getInstance() {
        if (instance == null || ExpectedConditions.stalenessOf(instance).apply(driver)) {
            instance = getFreshInstance();
        }

        return Objects.requireNonNull(instance, "instance must not be null");
    }

    private static class IndexedRefreshingWebElement extends RefreshingWebElement {
        private static final WeakHashMap<WebElementListIdentifier, List<WebElement>> instanceLists = new WeakHashMap<>();
        private final WebElementListIdentifier listIdentifier;
        private final int index;

        private IndexedRefreshingWebElement(WebDriver driver, @Nullable RefreshingWebElement parent, By locator, int index) {
            super(driver, parent, locator);
            if (index < 0) {
                throw new IllegalArgumentException("Argument 'index' must be a non-negative integer");
            }
            else {
                listIdentifier = getListIdentifier(parent, locator);
            }
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            IndexedRefreshingWebElement that = (IndexedRefreshingWebElement) o;
            return index == that.index && Objects.equals(listIdentifier, that.listIdentifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), listIdentifier, index);
        }

        @Override
        protected WebElement getFreshInstance() {
            List<WebElement> matches = instanceLists.getOrDefault(listIdentifier, new ArrayList<>());
            if (matches.size() <= index || ExpectedConditions.stalenessOf(matches.get(index)).apply(driver)) {
                updateListInstances();
            }

            if (matches.size() <= index) {
                throw new NoSuchElementException("No element with index " + index + " found for locator: " + locator);
            }

            return matches.get(index);
        }

        private static int updateListInstances(WebDriver driver, WebElementListIdentifier listIdentifier, @Nullable RefreshingWebElement parent, By locator) {
            List<WebElement> matches;
            if (parent == null) {
                matches = driver.findElements(locator);
            }
            else  {
                matches = parent.getInstance().findElements(locator);
            }

            if (matches.isEmpty()) {
                instanceLists.remove(listIdentifier);
            }
            else {
                instanceLists.put(listIdentifier, matches);
            }

            return matches.size();
        }

        private int updateListInstances() {
            return updateListInstances(driver, listIdentifier, parent, locator);
        }

        public static WebElementListIdentifier getListIdentifier(@Nullable RefreshingWebElement parent, By locator) {
            final WebElementListIdentifier newIdentifier = new WebElementListIdentifier(parent, locator);
            Optional<WebElementListIdentifier> exactKeyObj = instanceLists.keySet().stream()
                    .filter(key -> key.equals(newIdentifier))
                    .findFirst();
            return exactKeyObj.orElse(newIdentifier);
        }

        private static class WebElementListIdentifier {
            @Nullable public final RefreshingWebElement parent;
            public final By locator;

            public WebElementListIdentifier(@Nullable RefreshingWebElement parent, By locator) {
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
}

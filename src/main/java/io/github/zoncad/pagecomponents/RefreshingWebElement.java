package io.github.zoncad.pagecomponents;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

@NullMarked
public abstract class RefreshingWebElement implements WebElement {
    private static final int MAX_REFRESHES = 1;
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
        return new SpecificRefreshingWebElement(driver, parent, locator);
    }

    public static RefreshingWebElement of(WebDriver driver, @Nullable RefreshingWebElement parent, By locator, @Nullable Integer index) {
        if (index == null) {
            return of(driver, parent, locator);
        }
        else {
            return new IndexedRefreshingWebElement(driver, parent, locator, index);
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
        getInstanceAndThenPerform(WebElement::click);
    }

    @Override
    public void submit() {
        getInstanceAndThenPerform(WebElement::submit);
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        getInstanceAndThenPerform(element -> element.sendKeys(keysToSend));
    }

    @Override
    public void clear() {
        getInstanceAndThenPerform(WebElement::clear);
    }

    @Override
    public String getTagName() {
        return getInstanceAndThenGetValue(WebElement::getTagName);
    }

    @Override
    @Nullable
    public String getDomProperty(String name) {
        return getInstanceAndThenGetValue(element -> element.getDomProperty(name));
    }

    @Override
    @Nullable
    public String getDomAttribute(String name) {
        return getInstanceAndThenGetValue(element -> element.getDomAttribute(name));
    }

    @Override
    @Nullable
    public String getAttribute(String name) {
        return getInstanceAndThenGetValue(element -> element.getAttribute(name));
    }

    @Override
    @Nullable
    public String getAriaRole() {
        return getInstanceAndThenGetValue(WebElement::getAriaRole);
    }

    @Override
    @Nullable
    public String getAccessibleName() {
        return getInstanceAndThenGetValue(WebElement::getAccessibleName);
    }

    @Override
    public boolean isSelected() {
        return getInstanceAndThenGetValue(WebElement::isSelected);
    }

    @Override
    public boolean isEnabled() {
        return getInstanceAndThenGetValue(WebElement::isEnabled);
    }

    @Override
    public String getText() {
        return getInstanceAndThenGetValue(WebElement::getText);
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
        return getInstanceAndThenGetValue(WebElement::getShadowRoot);
    }

    @Override
    public boolean isDisplayed() {
        return getInstanceAndThenGetValue(WebElement::isDisplayed);
    }

    @Override
    public Point getLocation() {
        return getInstanceAndThenGetValue(WebElement::getLocation);
    }

    @Override
    public Dimension getSize() {
        return getInstanceAndThenGetValue(WebElement::getSize);
    }

    @Override
    public Rectangle getRect() {
        return getInstanceAndThenGetValue(WebElement::getRect);
    }

    @Override
    public String getCssValue(String propertyName) {
        return getInstanceAndThenGetValue(element -> element.getCssValue(propertyName));
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return getInstanceAndThenGetValue(element -> element.getScreenshotAs(target));
    }

    abstract protected WebElement getFreshInstance();

    private WebElement getInstance() {
        if (instance == null) {
            instance = getFreshInstance();
        }

        return instance;
    }

    @NullUnmarked
    private <R> R getInstanceAndThenGetValue(Function<WebElement, R> valueMapper) {
        for (int i = 0; i <= MAX_REFRESHES; ++i) {
            try {
                return valueMapper.apply(getInstance());
            } catch (StaleElementReferenceException ex) {
                instance = null;
            }
        }
        throw new StaleElementReferenceException("Element reference stale after " + MAX_REFRESHES + " refreshes");
    }

    @NullUnmarked
    private void getInstanceAndThenPerform(Consumer<WebElement> action) {
        for (int i = 0; i <= MAX_REFRESHES; ++i) {
            try {
                action.accept(getInstance());
                return;
            } catch (StaleElementReferenceException ex) {
                instance = null;
            }
        }
        throw new StaleElementReferenceException("Element reference stale after " + MAX_REFRESHES + " refreshes");
    }

    private static class SpecificRefreshingWebElement extends RefreshingWebElement {
        private SpecificRefreshingWebElement(WebDriver driver, @Nullable RefreshingWebElement parent, By locator) {
            super(driver, parent, locator);
            instance = getFreshInstance();
        }

        @Override
        protected WebElement getFreshInstance() {
            WebElement freshInstance;
            if (parent == null) {
                freshInstance = driver.findElement(locator);
            }
            else  {
                freshInstance = parent.getInstanceAndThenGetValue(element -> element.findElement(locator));
            }

            return freshInstance;
        }
    }

    private static class IndexedRefreshingWebElement extends RefreshingWebElement {
        // TODO: Make instanceLists synchronized
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
            instance = getFreshInstance();
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
            // TODO: Reevaluate to ensure instance returned is fresh
            List<WebElement> matches = instanceLists.getOrDefault(listIdentifier, new ArrayList<>());
            if (matches.size() <= index || instance == matches.get(index)) {
                updateListInstances(driver, listIdentifier, parent, locator);
                matches = instanceLists.getOrDefault(listIdentifier, new ArrayList<>());
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
            else {
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

        public static WebElementListIdentifier getListIdentifier(@Nullable RefreshingWebElement parent, By locator) {
            final WebElementListIdentifier newIdentifier = new WebElementListIdentifier(parent, locator);
            Optional<WebElementListIdentifier> exactKeyObj = instanceLists.keySet().stream()
                    .filter(key -> key.equals(newIdentifier))
                    .findFirst();
            return exactKeyObj.orElse(newIdentifier);
        }

        private record WebElementListIdentifier(@Nullable RefreshingWebElement parent, By locator) {

        }
    }
}

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
    private static final int MAX_REFRESHES = 2;
    protected final SearchContext searchContext;
    protected final By locator;

    @Nullable protected WebElement instance;

    protected RefreshingWebElement(SearchContext searchContext, By locator) {
        this.searchContext = searchContext;
        this.locator = locator;
    }

    public static RefreshingWebElement locatedBy(SearchContext searchContext, By locator, boolean lazilyLocate) {
        return locatedBy(searchContext, locator, null, lazilyLocate);
    }

    public static RefreshingWebElement locatedBy(SearchContext searchContext, By locator, @Nullable Integer index, boolean lazilyLocate) {
        if (index == null) {
            return new SpecificRefreshingWebElement(searchContext, locator, lazilyLocate);
        }
        else {
            return new IndexedRefreshingWebElement(searchContext, locator, index, lazilyLocate);
        }
    }

    public static RefreshingWebElement locatedBy(SearchContext searchContext, By locator) {
        return locatedBy(searchContext, locator, false);
    }

    public static RefreshingWebElement locatedBy(SearchContext searchContext, By locator, @Nullable Integer index) {
        return locatedBy(searchContext, locator, index, false);
    }

    public static RefreshingWebElement lazilyLocatedBy(SearchContext searchContext, By locator) {
        return lazilyLocatedBy(searchContext, locator, null);
    }

    public static RefreshingWebElement lazilyLocatedBy(SearchContext searchContext, By locator, @Nullable Integer index) {
        if (index == null) {
            return locatedBy(searchContext, locator, true);
        }
        else {
            return locatedBy(searchContext, locator, index, true);
        }
    }

    public static List<RefreshingWebElement> listLocatedBy(SearchContext searchContext, By locator) {
        int numMatches = WebElementListCache.updateListInstances(searchContext, locator);

        return IntStream.range(0, numMatches)
                .mapToObj(i -> locatedBy(searchContext, locator, i))
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RefreshingWebElement that)) return false;
        return Objects.equals(searchContext, that.searchContext) && Objects.equals(locator, that.locator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchContext, locator);
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
        return new ArrayList<>(listLocatedBy(this, locator));
    }

    @Override
    public WebElement findElement(By locator) {
        return locatedBy(this, locator);
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

    public WebElement getInstance() {
        if (instance == null) {
            instance = getFreshInstance();
        }

        return instance;
    }

    abstract protected WebElement getFreshInstance();

    @NullUnmarked
    private <R> R getInstanceAndThenGetValue(Function<WebElement, R> valueMapper) {
        for (int i = 0; i <= MAX_REFRESHES; ++i) {
            try {
                return valueMapper.apply(getInstance());
            } catch (StaleElementReferenceException ex) {
                instance = null;
            }
        }
        throw new StaleElementReferenceException("Element reference stale after " + MAX_REFRESHES + " refresh" + (MAX_REFRESHES == 1 ? "" : "es"));
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
        throw new StaleElementReferenceException("Element reference stale after " + MAX_REFRESHES + " refresh" + (MAX_REFRESHES == 1 ? "" : "es"));
    }

    private static class SpecificRefreshingWebElement extends RefreshingWebElement {
        private SpecificRefreshingWebElement(SearchContext searchContext, By locator, boolean lazyInitialization) {
            super(searchContext, locator);

            if (!lazyInitialization) {
                instance = getFreshInstance();
            }
        }

        @Override
        protected WebElement getFreshInstance() {
            WebElement freshInstance = null;
            if (searchContext instanceof RefreshingWebElement) {
                freshInstance = ((RefreshingWebElement) searchContext).getInstanceAndThenGetValue(element -> element.findElement(locator));
            }
            else {
                freshInstance = searchContext.findElement(locator);
            }

            return freshInstance;
        }
    }

    private static class IndexedRefreshingWebElement extends RefreshingWebElement {
        private final WebElementListCache.ListIdentifier listIdentifier;
        private final int index;

        private IndexedRefreshingWebElement(SearchContext searchContext, By locator, int index, boolean lazyInitialization) {
            super(searchContext, locator);
            if (index < 0) {
                throw new IllegalArgumentException("Argument 'index' must be a non-negative integer");
            }

            this.index = index;
            listIdentifier = WebElementListCache.getListIdentifier(searchContext, locator);

            if (!lazyInitialization) {
                instance = getFreshInstance();
            }
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
            List<WebElement> matches;
            synchronized (listIdentifier) {
                matches = WebElementListCache.getListInstances(listIdentifier);
                if (matches.size() <= index || instance == matches.get(index)) {
                    WebElementListCache.updateListInstances(listIdentifier, searchContext, locator);
                    matches = WebElementListCache.getListInstances(listIdentifier);
                }
            }

            if (matches.size() <= index) {
                throw new NoSuchElementException("No element with index " + index + " found for locator: " + locator);
            }

            return matches.get(index);
        }
    }

    private static class WebElementListCache {
        private static final Map<ListIdentifier, List<WebElement>> instanceLists = Collections.synchronizedMap(new WeakHashMap<>());

        private static ListIdentifier getListIdentifier(SearchContext searchContext, By locator) {
            final ListIdentifier newIdentifier = new ListIdentifier(searchContext, locator);
            Set<ListIdentifier> keys = instanceLists.keySet();
            Optional<ListIdentifier> exactKeyObj;

            synchronized (instanceLists) {
                exactKeyObj = keys.stream()
                        .filter(key -> key.equals(newIdentifier))
                        .findFirst();
                if (exactKeyObj.isEmpty()) {
                    instanceLists.put(newIdentifier, new ArrayList<>());
                }
            }

            return exactKeyObj.orElse(newIdentifier);
        }

        private static List<WebElement> getListInstances(ListIdentifier listIdentifier) {
            return Objects.requireNonNull(instanceLists.get(listIdentifier), "ListIdentifier is missing from list cache");
        }

        private static int updateListInstances(ListIdentifier listIdentifier, SearchContext searchContext, By locator) {
            List<WebElement> matches;
            if (searchContext instanceof RefreshingWebElement) {
                matches = ((RefreshingWebElement) searchContext).getInstance().findElements(locator);
            }
            else {
                matches = searchContext.findElements(locator);
            }

            instanceLists.put(listIdentifier, matches);

            return matches.size();
        }

        private static int updateListInstances(SearchContext searchContext, By locator) {
            return updateListInstances(getListIdentifier(searchContext, locator), searchContext, locator);
        }

        private record ListIdentifier(SearchContext searchContext, By locator) { }
    }
}

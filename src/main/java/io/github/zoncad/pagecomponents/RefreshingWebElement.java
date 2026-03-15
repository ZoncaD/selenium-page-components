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

/**
 * A WebElement that can refresh itself with a new instance when stale.
 */
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

    /**
     * Creates a RefreshingWebElement wrapper for the WebElement located by the given locator within the searchContext.
     * @param searchContext context to search within, such as a WebDriver or a parent RefreshingWebElement
     * @param locator locator used to identify WebElement
     * @param lazilyLocate boolean indicating when to first attempt to locate the WebElement. If true, the WebElement is
     *                    located immediately. If false, the WebElement will only be located the first time it is used.
     * @return the RefreshingWebElement
     */
    public static RefreshingWebElement locatedBy(SearchContext searchContext, By locator, boolean lazilyLocate) {
        return locatedBy(searchContext, locator, null, lazilyLocate);
    }

    /**
     * Creates a RefreshingWebElement wrapper for the WebElement located by the given locator and index within the
     * searchContext. If no index is provided, behaves exactly like {@link #locatedBy(SearchContext, By, boolean)}.
     * @param searchContext context to search within, such as a WebDriver or a parent RefreshingWebElement
     * @param locator locator used to identify WebElement
     * @param index null or the non-negative index of the desired WebElement in the list of matches.
     * @param lazilyLocate boolean indicating when to first attempt to locate the WebElement. If true, the WebElement is
     *                    located immediately. If false, the WebElement will only be located the first time it is used.
     * @return the RefreshingWebElement
     */
    public static RefreshingWebElement locatedBy(SearchContext searchContext, By locator, @Nullable Integer index, boolean lazilyLocate) {
        if (index == null) {
            return new SpecificRefreshingWebElement(searchContext, locator, lazilyLocate);
        }
        else {
            return new IndexedRefreshingWebElement(searchContext, locator, index, lazilyLocate);
        }
    }

    /**
     * Creates an eagerly initialized RefreshingWebElement wrapper for the WebElement located by the given locator
     * within the searchContext.
     * @param searchContext context to search within, such as a WebDriver or a parent RefreshingWebElement
     * @param locator locator used to identify WebElement
     * @return the RefreshingWebElement
     */
    public static RefreshingWebElement locatedBy(SearchContext searchContext, By locator) {
        return locatedBy(searchContext, locator, false);
    }

    /**
     * Creates an eagerly initialized RefreshingWebElement wrapper for the WebElement located by the given locator and
     * index within the searchContext. If no index is provided, behaves exactly like {@link #locatedBy(SearchContext, By)}.
     * @param searchContext context to search within, such as a WebDriver or a parent RefreshingWebElement
     * @param locator locator used to identify WebElement
     * @param index null or the non-negative index of the desired WebElement in the list of matches.
     * @return the RefreshingWebElement
     */
    public static RefreshingWebElement locatedBy(SearchContext searchContext, By locator, @Nullable Integer index) {
        return locatedBy(searchContext, locator, index, false);
    }

    /**
     * Creates an eagerly initialized RefreshingWebElement wrapper for the WebElement located by the given locator
     * within the searchContext.
     * @param searchContext context to search within, such as a WebDriver or a parent RefreshingWebElement
     * @param locator locator used to identify WebElement
     * @return the RefreshingWebElement
     */
    public static RefreshingWebElement lazilyLocatedBy(SearchContext searchContext, By locator) {
        return lazilyLocatedBy(searchContext, locator, null);
    }

    /**
     * Creates a lazily initialized RefreshingWebElement wrapper for the WebElement located by the given locator and
     * index within the searchContext. If no index is provided, behaves exactly like {@link #lazilyLocatedBy(SearchContext, By)}.
     * @param searchContext context to search within, such as a WebDriver or a parent RefreshingWebElement
     * @param locator locator used to identify WebElement
     * @param index null or the non-negative index of the desired WebElement in the list of matches.
     * @return the RefreshingWebElement
     */
    public static RefreshingWebElement lazilyLocatedBy(SearchContext searchContext, By locator, @Nullable Integer index) {
        if (index == null) {
            return locatedBy(searchContext, locator, true);
        }
        else {
            return locatedBy(searchContext, locator, index, true);
        }
    }

    /**
     * Returns a list of eagerly initialized RefreshingWebElement wrappers for the WebElements located by the given locator within the searchContext.
     * @param searchContext context to search within, such as a WebDriver or a parent RefreshingWebElement
     * @param locator locator used to identify WebElements
     * @return the list of RefreshingWebElements
     */
    public static List<RefreshingWebElement> listLocatedBy(SearchContext searchContext, By locator) {
        int numMatches = WebElementListCache.updateListInstances(searchContext, locator);

        return IntStream.range(0, numMatches)
                .mapToObj(i -> locatedBy(searchContext, locator, i))
                .toList();
    }

    /**
     * Get an instance of the WebElement this RefreshingWebElement represents. No guarantee that returned instance is not
     * stale.
     * @return the unwrapped WebElement
     */
    public WebElement getInstance() {
        if (instance == null) {
            instance = getFreshInstance();
        }

        return instance;
    }

    /**
     * Returns an eagerly initialized RefreshingWebElement wrapper for the WebElement located by the given locator.
     * Equivalent to calling {@link #locatedBy(SearchContext, By)} with this RefreshingWebElement as the SearchContext.
     * @param locator locator used to identify WebElement
     * @return the wrapped WebElement
     */
    @Override
    public WebElement findElement(By locator) {
        return locatedBy(this, locator);
    }

    /**
     * Returns a list of eagerly initialized RefreshingWebElement wrappers for the WebElements located by the given
     * locator. Uses this RefreshingWebElement as the SearchContext.
     * @param locator locator used to identify WebElements
     * @return list of wrapped WebElements
     */
    @Override
    public List<WebElement> findElements(By locator) {
        return new ArrayList<>(listLocatedBy(this, locator));
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RefreshingWebElement that)) return false;
        return Objects.equals(searchContext, that.searchContext) && Objects.equals(locator, that.locator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchContext, locator);
    }

    /**
     * Locate a new instance of the wrapped WebElement
     * @return the new WebElement
     */
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

    /**
     * A RefreshingWebElement wrapper for the WebElement located by the given locator within the searchContext.
     */
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

    /**
     * A RefreshingWebElement wrapper for the WebElement at position index within the list of results located by the
     * given locator within the searchContext.
     */
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

        /**
         * Gets a newer instance of the wrapped WebElement. May not be fresh if cache contains newer instance that
         * happens to be stale.
         * @return the newer WebElement
         */
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

    /**
     * Cache of WebElements used for index-based RefreshingWebElements.
     */
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

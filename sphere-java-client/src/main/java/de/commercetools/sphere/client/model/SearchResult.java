package de.commercetools.sphere.client.model;

import de.commercetools.internal.Defaults;
import de.commercetools.sphere.client.facets.*;
import de.commercetools.sphere.client.model.facets.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/** Result of a search query to the Sphere backend. */
public class SearchResult<T> {
    private int offset;
    private int count;
    private int total;
    private List<T> results;
    @JsonProperty("facets")
    private Map<String, FacetResult> facets;

    public int getOffset() { return offset; }
    public int getCount() { return count; }
    public int getTotal() { return total; }
    public List<T> getResults() { return results; }

    public SearchResult(int offset, int count, int total, Collection<T> results, Map<String, FacetResult> facets) {
        this.offset = offset;
        this.count = count;
        this.total = total;
        this.results = new ArrayList<T>(results);
        this.facets = facets;
    }

    // for JSON deserializer
    private SearchResult() {}

    // --------------------------------------------------------------
    // Returning correct facet type based on facet type
    // --------------------------------------------------------------

    public TermFacetResult getFacet(TermFacet facet) {
        return getTermsFacet(facet.getAttributeName());
    }

    public RangeFacetResult getFacet(RangeFacet facet) {
        return getRangeFacet(facet.getAttributeName());
    }

    public DateRangeFacetResult getFacet(DateRangeFacet facet) {
        return getDateRangeFacet(facet.getAttributeName());
    }

    public TimeRangeFacetResult getFacet(TimeRangeFacet facet) {
        return getTimeRangeFacet(facet.getAttributeName());
    }

    public DateTimeRangeFacetResult getFacet(DateTimeRangeFacet facet) {
        return getDateTimeRangeFacet(facet.getAttributeName());
    }

    /** Gets generic facet result for given facet.
     *  This is a low-level method. You will have to downcast to process the result.
     *  Please refer to {@link #getFacet} if you know the actual facet type. */
    public FacetResult getFacetGeneric(Facet facet) {
        return getFacetRaw(facet.getAttributeName());
    }

    // --------------------------------------------------------------
    // Low-level API for returning the raw facets parsed from JSON.
    // We might decide to make it public.
    // --------------------------------------------------------------

    public Map<String, FacetResult> getFacetsRaw() {
        return facets;
    }

    public FacetResult getFacetRaw(String expression) {
        return facets.get(expression);
    }

    /** Gets a terms facet result for given facet expression. */
    public TermFacetResult getTermsFacet(String expression) {
        FacetResult facetResult = getFacetRaw(expression);
        if (facetResult == null)
            return null;
        checkCorrectType(expression, TermFacetResult.class, facetResult);
        return (TermFacetResult)facetResult;
    }

    /** Gets a range facet result for given facet expression. */
    public RangeFacetResult getRangeFacet(String expression) {
        FacetResult facetResult = getFacetRaw(expression);
        if (facetResult == null)
            return null;
        checkCorrectType(expression, RangeFacetResult.class, facetResult);
        return (RangeFacetResult)facetResult;
    }

    /** Gets a values facet result for given facet expression. */
    public ValueFacetResult getValueFacet(String expression) {
        String prefix = expression + Defaults.valueFacetAliasSeparator;
        int prefixLen = prefix.length();
        List<ValueFacetItem> facetItems = new ArrayList<ValueFacetItem>();
        for (Map.Entry<String, FacetResult> facetResultEntry: getFacetsRaw().entrySet()) {
            if (facetResultEntry.getKey().startsWith(prefix)) {
                facetItems.add(new ValueFacetItem(
                        facetResultEntry.getKey().substring(prefixLen),
                        ((ValueFacetResultRaw)facetResultEntry.getValue()).getCount()));
            }
        }
        return new ValueFacetResult(facetItems);
    }

    /** Gets a date range facet result for given facet expression. */
    public DateRangeFacetResult getDateRangeFacet(String expression) {
        // Search returns facets in milliseconds
        return DateRangeFacetResult.fromMilliseconds(getRangeFacet(expression));
    }

    /** Gets a time range facet result for given facet expression. */
    public TimeRangeFacetResult getTimeRangeFacet(String expression) {
        // Search returns facets in milliseconds
        return TimeRangeFacetResult.fromMilliseconds(getRangeFacet(expression));
    }

    /** Gets a time range facet result for given facet expression. */
    public DateTimeRangeFacetResult getDateTimeRangeFacet(String expression) {
        // Search returns facets in milliseconds
        return DateTimeRangeFacetResult.fromMilliseconds(getRangeFacet(expression));
    }

    /** Before downcasting, checks that the type of result is correct. */
    private void checkCorrectType(String attributeName, Class<?> expectedClass, FacetResult facetResult) {
        if (!(expectedClass.isInstance(facetResult))) {
            throw new RuntimeException(attributeName + " is a " + facetResult.getClass().getSimpleName() + ", not " + expectedClass.getSimpleName());
        }
    }
}

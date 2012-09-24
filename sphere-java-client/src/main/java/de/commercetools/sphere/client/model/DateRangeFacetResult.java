package de.commercetools.sphere.client.model;

import net.jcip.annotations.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.FluentIterable;

import java.util.List;
import java.util.ArrayList;

/** Information about a date range facet, returned as a part of {@link SearchResult}. */
@Immutable
public class DateRangeFacetResult implements FacetResult {
    private ImmutableList<DateRangeCount> ranges;

    /** A list of individual ranges for this date range facet and their respective counts. */
    public List<DateRangeCount> getRanges() {
        return ranges;
    }

    private DateRangeFacetResult(ImmutableList<DateRangeCount> ranges) {
        this.ranges = ranges;
    }

    /** Parses dates returned by the backend as milliseconds into joda.LocalDate instances. */
    static DateRangeFacetResult fromMilliseconds(RangeFacetResult facetResult) {
        return new DateRangeFacetResult(
                FluentIterable.from(facetResult.getRanges()).transform(DateRangeCount.fromMilliseconds).toImmutableList());
    }
}
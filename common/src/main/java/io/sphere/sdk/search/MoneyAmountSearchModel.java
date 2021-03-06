package io.sphere.sdk.search;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Arrays.asList;

public class MoneyAmountSearchModel<T, S extends SearchSortDirection> extends SearchModelImpl<T> implements RangeTermModel<T, BigDecimal>, SearchSortingModel<T, S> {

    public MoneyAmountSearchModel(final Optional<? extends SearchModel<T>> parent, final String pathSegment) {
        super(parent, pathSegment);
    }

    @Override
    public RangedFilterSearchModel<T, BigDecimal> filter() {
        return new RangedFilterSearchModel<>(Optional.of(this), Optional.empty(), TypeSerializer.ofMoneyAmount());
    }

    @Override
    public RangedFacetSearchModel<T, BigDecimal> facet() {
        return new RangedFacetSearchModel<>(Optional.of(this), Optional.empty(), TypeSerializer.ofMoneyAmount());
    }

    @Override
    public SearchSort<T> sort(S sortDirection) {
        if (hasPath(asList("variants", "price", "centAmount"))) {
            return new SphereSearchSort<>(new MoneyAmountSearchModel<>(Optional.empty(), "price"), sortDirection);
        } else {
            return new SphereSearchSort<>(this, sortDirection);
        }
    }


}

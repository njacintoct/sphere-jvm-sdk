package io.sphere.sdk.taxcategories;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.queries.DefaultModelQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.QueryDsl;

public class TaxCategoryQuery extends DefaultModelQuery<TaxCategory, TaxCategoryQueryModel<TaxCategory>> {
    TaxCategoryQuery(){
        super("/tax-categories", new TypeReference<PagedQueryResult<TaxCategory>>(){
            @Override
            public String toString() {
                return "TypeReference<PagedQueryResult<TaxCategory>>";
            }
        });
    }

    public QueryDsl<TaxCategory, TaxCategoryQueryModel<TaxCategory>> byName(final String name) {
        return withPredicate(TaxCategoryQueryModel.get().name().is(name));
    }
}
package io.sphere.sdk.queries;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collections;
import java.util.List;

public class DefaultModelQuery<I> extends QueryDslImpl<I> {
    public DefaultModelQuery(final String endpoint, final TypeReference<PagedQueryResult<I>> pagedQueryResultTypeReference) {
        this(endpoint, Collections.emptyList(), pagedQueryResultTypeReference);
    }

    public DefaultModelQuery(final String endpoint, final List<QueryParameter> additionalQueryParameters, final TypeReference<PagedQueryResult<I>> pagedQueryResultTypeReference) {
        super(endpoint, additionalQueryParameters, pagedQueryResultTypeReference);
    }
}

package io.sphere.sdk.producttypes;

import java.util.Optional;
import io.sphere.sdk.queries.EmbeddedQueryModel;
import io.sphere.sdk.queries.QueryModel;
import io.sphere.sdk.queries.StringQueryModel;
import io.sphere.sdk.queries.StringQuerySortingModel;

public final class AttributeDefinitionQueryModel extends EmbeddedQueryModel<ProductType> {

    private static final AttributeDefinitionQueryModel instance =
            new AttributeDefinitionQueryModel(Optional.empty(), Optional.<String>empty());

    public static AttributeDefinitionQueryModel get() {
        return instance;
    }

    AttributeDefinitionQueryModel(Optional<? extends QueryModel<ProductType>> parent, Optional<String> pathSegment) {
        super(parent, pathSegment);
    }

    public StringQueryModel<ProductType> name() {
        return new StringQuerySortingModel<>(Optional.of(this), "name");
    }

    public AttributeTypeQueryModel<ProductType> type() {
        return new AttributeTypeQueryModel<ProductType>(Optional.of(this), Optional.of("type"));
    }
}
package example;

import io.sphere.sdk.attributes.AttributeDefinition;
import io.sphere.sdk.attributes.AttributeType;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import io.sphere.sdk.attributes.EnumType;
import io.sphere.sdk.models.PlainEnumValue;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QueryByProductTypeNameExample {
    private final SphereClient client = null;//TODO

    public void queryByNameExample() {
        CompletableFuture<PagedQueryResult<ProductType>> queryResultFuture = client.execute(ProductTypeQuery.of().byName("t-shirt"));
        CompletableFuture<List<PlainEnumValue>> possibleValuesFuture = queryResultFuture.thenApply(
                queryResult -> extractPossibleEnumValuesForSize(queryResult));
    }

    private static List<PlainEnumValue> extractPossibleEnumValuesForSize(PagedQueryResult<ProductType> pagedQueryResult) {
        ProductType productType = pagedQueryResult.
                head().
                orElseThrow(MissingProductTypeException::new);

        AttributeDefinition sizeAttribute = productType.
                getAttribute("size").
                orElseThrow(MissingAttributeException::new);

        if (sizeAttribute.getAttributeType() instanceof EnumType) {
            return ((EnumType) sizeAttribute.getAttributeType()).getValues();
        } else {
            throw new UnexpectedAttributeTypeException("size", EnumType.class, sizeAttribute.getAttributeType());
        }
    }

    public static class MissingProductTypeException extends RuntimeException {
        private static final long serialVersionUID = 4954918890077093840L;
    }

    public static class MissingAttributeException extends RuntimeException {
        private static final long serialVersionUID = 4954918890077093841L;
    }

    public static class UnexpectedAttributeTypeException extends RuntimeException {
        private static final long serialVersionUID = 4954918890077093842L;

        private final String attributeName;
        private final Class<? extends AttributeType> expectedType;
        private final AttributeType actualType;

        public UnexpectedAttributeTypeException(String attributeName, Class<? extends AttributeType> expectedType, AttributeType actualType) {
            super("Unexpected type of attribute with name '" + attributeName + "'. Expected type is '" +
                    expectedType.getSimpleName() + "', but actual type was '" + actualType + "'.");

            this.attributeName = attributeName;
            this.expectedType = expectedType;
            this.actualType = actualType;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public Class<? extends AttributeType> getExpectedType() {
            return expectedType;
        }

        public AttributeType getActualType() {
            return actualType;
        }
    }
}
package io.sphere.sdk.producttypes.commands;

import io.sphere.sdk.http.JsonEndpoint;
import io.sphere.sdk.producttypes.ProductType;

final class ProductTypesEndpoint {
    static JsonEndpoint<ProductType> ENDPOINT = JsonEndpoint.of(ProductType.typeReference(), "/product-types");
}
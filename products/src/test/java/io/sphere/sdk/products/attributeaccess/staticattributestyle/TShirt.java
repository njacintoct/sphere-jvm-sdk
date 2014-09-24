package io.sphere.sdk.products.attributeaccess.staticattributestyle;

import io.sphere.sdk.attributes.AttributeGetterSetter;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.attributes.AttributeTypes;
import io.sphere.sdk.products.Product;

public class TShirt {
    public static final AttributeGetterSetter<Product, LocalizedString> LONG_DESCRIPTION = AttributeTypes.ofLocalizedString().getterSetter("longDescription");
}
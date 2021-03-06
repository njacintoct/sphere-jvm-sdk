package io.sphere.sdk.products;

import io.sphere.sdk.attributes.Attribute;
import io.sphere.sdk.attributes.AttributeGetter;
import io.sphere.sdk.attributes.AttributeMapper;
import io.sphere.sdk.json.JsonException;
import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

class AttributeContainerImpl extends Base implements AttributeContainer {
    private final List<Attribute> attributes;

    protected AttributeContainerImpl(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public List<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public <T> Optional<T> getAttribute(final AttributeGetter<Product, T> accessor) {
        final String attributeName = accessor.getName();
        final Optional<Attribute> attributeOption = getAttributes().stream()
                .filter(a -> Objects.equals(attributeName, a.getName()))
                .findFirst();

        return attributeOption.map(attribute -> {
            final AttributeMapper<T> mapper = accessor.getMapper();

            try {
                return attribute.getValue(mapper);
            } catch (final JsonException e) {
                throw transformError(e, attributeName, mapper);
            }
        });
    }

    protected JsonException transformError(JsonException e, String attributeName, AttributeMapper<?> mapper) {
        return new JsonException(format("AttributeContainer does not contain an attribute '%s' which can be mapped with %s.", attributeName, mapper), e.getCause());
    }

    public static AttributeContainerImpl of(List<Attribute> attributes) {
        return new AttributeContainerImpl(attributes);
    }
}

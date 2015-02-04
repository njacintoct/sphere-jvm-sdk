package io.sphere.sdk.attributes;

import io.sphere.sdk.models.LocalizedStrings;

public class ReferenceAttributeDefinition extends AttributeDefinitionBase<ReferenceType> {
    ReferenceAttributeDefinition(final ReferenceType attributeType, final String name, final LocalizedStrings label, final boolean isRequired, final AttributeConstraint attributeConstraint, final boolean isSearchable) {
        super(attributeType, name, label, isRequired, attributeConstraint, isSearchable);
    }
}
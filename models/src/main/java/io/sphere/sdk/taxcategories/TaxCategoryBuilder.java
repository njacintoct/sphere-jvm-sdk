package io.sphere.sdk.taxcategories;

import io.sphere.sdk.models.DefaultModelFluentBuilder;

import java.util.List;
import java.util.Optional;

public class TaxCategoryBuilder extends DefaultModelFluentBuilder<TaxCategoryBuilder, TaxCategory> {
    private final String name;
    private Optional<String> description = Optional.empty();
    private final List<TaxRate> taxRates;

    private TaxCategoryBuilder(final String name, final List<TaxRate> taxRates) {
        this.name = name;
        this.taxRates = taxRates;
    }

    public static TaxCategoryBuilder of(final TaxCategoryDraft taxCategoryDraft) {
        return of(taxCategoryDraft.getName(), taxCategoryDraft.getTaxRates()).description(taxCategoryDraft.getDescription());
    }

    public static TaxCategoryBuilder of(final String name, final List<TaxRate> taxRates) {
        return new TaxCategoryBuilder(name, taxRates);
    }

    public TaxCategoryBuilder description(final Optional<String> description) {
        this.description = description;
        return getThis();
    }

    public TaxCategoryBuilder description(final String description) {
        return description(Optional.ofNullable(description));
    }

    @Override
    protected TaxCategoryBuilder getThis() {
        return this;
    }

    @Override
    public TaxCategory build() {
        return new TaxCategoryImpl(id, version, createdAt, lastModifiedAt, name, description, taxRates);
    }
}

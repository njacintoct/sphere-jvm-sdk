package io.sphere.sdk.shippingmethods.commands.updateactions;

import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.taxcategories.TaxCategory;

/**
 *
 * {@include.example io.sphere.sdk.shippingmethods.commands.ShippingMethodUpdateCommandTest#changeTaxCategory()}
 */
public class ChangeTaxCategory extends UpdateAction<ShippingMethod> {
    private final Reference<TaxCategory> taxCategory;

    private ChangeTaxCategory(final Reference<TaxCategory> taxCategory) {
        super("changeTaxCategory");
        this.taxCategory = taxCategory;
    }

    public Reference<TaxCategory> getTaxCategory() {
        return taxCategory;
    }

    public static ChangeTaxCategory of(final Referenceable<TaxCategory> taxCategory) {
        return new ChangeTaxCategory(taxCategory.toReference());
    }
}

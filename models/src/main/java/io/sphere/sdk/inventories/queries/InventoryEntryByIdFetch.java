package io.sphere.sdk.inventories.queries;

import io.sphere.sdk.inventories.InventoryEntry;
import io.sphere.sdk.queries.ByIdFetchImpl;

public class InventoryEntryByIdFetch extends ByIdFetchImpl<InventoryEntry> {
    private InventoryEntryByIdFetch(final String id) {
        super(id, Endpoint.ENDPOINT);
    }

    public static InventoryEntryByIdFetch of(final String id) {
        return new InventoryEntryByIdFetch(id);
    }
}

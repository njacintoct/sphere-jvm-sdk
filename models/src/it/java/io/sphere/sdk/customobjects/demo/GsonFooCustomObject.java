package io.sphere.sdk.customobjects.demo;

import io.sphere.sdk.customobjects.CustomObject;

import java.time.Instant;

public class GsonFooCustomObject implements CustomObject<GsonFoo> {
    private final String container;
    private final String key;
    private final GsonFoo value;
    private final String id;
    private final long version;

    public GsonFooCustomObject(final String container, final String key, final GsonFoo value, final String id, final long version) {
        this.container = container;
        this.key = key;
        this.value = value;
        this.id = id;
        this.version = version;
    }

    @Override
    public String getContainer() {
        return container;
    }

    @Override
    public Instant getCreatedAt() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Instant getLastModifiedAt() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public GsonFoo getValue() {
        return value;
    }

    @Override
    public long getVersion() {
        return version;
    }
}

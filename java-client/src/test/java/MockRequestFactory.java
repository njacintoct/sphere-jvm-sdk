package io.sphere.client;

import io.sphere.internal.request.RequestFactoryImpl;
import io.sphere.internal.request.RequestHolder;

/** Fake request construction for RequestFactoryImpl. */
public class MockRequestFactory extends RequestFactoryImpl {
    private String fakeResponseBody;
    private int fakeResponseStatus;

    public MockRequestFactory(String fakeResponseBody, int fakeResponseStatus) {
        super(null, null);
        this.fakeResponseBody = fakeResponseBody;
        this.fakeResponseStatus = fakeResponseStatus;
    }

    @Override protected <T> RequestHolder<T> createGet(String url) {
        return new MockRequestHolder<T>(url, "GET", fakeResponseStatus, fakeResponseBody);
    }

    @Override protected <T> RequestHolder<T> createPost(String url) {
        return new MockRequestHolder<T>(url, "POST", fakeResponseStatus, fakeResponseBody);
    }
}
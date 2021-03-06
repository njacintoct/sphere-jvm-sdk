package io.sphere.sdk.queries;

import io.sphere.sdk.annotations.Unsafe;

public interface Predicate<T> {
    Predicate<T> or(Predicate<T> other);

    Predicate<T> and(Predicate<T> other);

    /**
     * The predicate for the SPHERE.IO HTTP API, not url encoded.
     * Example: masterData(current(name(en="MB PREMIUM TECH T"))) and id = "e7ba4c75-b1bb-483d-94d8-2c4a10f78472"
     *
     * @return predicate as String
     */
    String toSphereQuery();

    @Unsafe
    static <T> Predicate<T> of(final String sphereQuery) {
        return new SimplePredicate<>(sphereQuery);
    }
}
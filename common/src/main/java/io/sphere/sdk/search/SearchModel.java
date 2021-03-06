package io.sphere.sdk.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SearchModel<T> {

    Optional<String> getPathSegment();

    Optional<? extends SearchModel<T>> getParent();

    default List<String> buildPath() {
        final List<String> pathSegments = new ArrayList<>();
        getParent().ifPresent(p -> pathSegments.addAll(p.buildPath()));
        getPathSegment().ifPresent(ps -> pathSegments.add(ps));
        return pathSegments;
    }

    /**
     * Checks if the given pathSegments matches the path of the parents and the current one of this search model.
     *
     * @param pathSegments the path segments, the most closer element is one the right side
     * @return true, if the path segments of this matches exactly pathSegments
     */
    default boolean hasPath(final List<String> pathSegments) {
        return buildPath().equals(pathSegments);
    }
}

package io.sphere.sdk.queries;

public interface Sort<T> {
    /**
     * returns a sort expression.
     * Example: dog.age asc
     * @return String with unescaped sphere sort expression
     */
    String toSphereSort();

    public static <T> Sort<T> of(final String sphereSortExpression) {
        return new SimpleSort<>(sphereSortExpression);
    }
}

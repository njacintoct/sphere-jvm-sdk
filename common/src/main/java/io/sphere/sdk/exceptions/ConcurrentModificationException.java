package io.sphere.sdk.exceptions;

/**
 * HTTP code 409 response from SPHERE.IO.
 *
 */
public class ConcurrentModificationException extends SphereServiceException {
    private static final long serialVersionUID = 0L;

    public ConcurrentModificationException() {
        super(409);
    }
}

package io.sphere.sdk.exceptions;

/**
 * Exception thrown when the Sphere authorization service responds with other status code than HTTP 200 OK.
 *
 */
public class AuthorizationException extends SphereException {
    private static final long serialVersionUID = 0L;

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }
}
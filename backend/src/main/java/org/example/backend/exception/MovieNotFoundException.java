package org.example.backend.exception;

/**
 * Thrown when OMDb reports no matching movie (its response carries
 * {@code "Response":"False"}, e.g. "Movie not found!").
 *
 * <p>Translated into an HTTP 404 by the exception handler in this package.
 */
public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(String message) {
        super(message);
    }
}

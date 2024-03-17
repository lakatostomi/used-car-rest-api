package org.usedcar.rest.webservice.exception;

public class ResourceDeletingIsNotAllowedException extends RuntimeException{
    public ResourceDeletingIsNotAllowedException() {
    }

    public ResourceDeletingIsNotAllowedException(String message) {
        super(message);
    }
}

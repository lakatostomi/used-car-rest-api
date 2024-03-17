package org.usedcar.rest.webservice.exception;

public class TokenIsRevokedException extends Exception {
    public TokenIsRevokedException() {
    }

    public TokenIsRevokedException(String message) {
        super(message);
    }
}

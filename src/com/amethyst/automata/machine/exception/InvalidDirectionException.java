package com.amethyst.automata.machine.exception;

public class InvalidDirectionException extends AutomatonException {
    public InvalidDirectionException(String message) {
        super(message);
    }

    public InvalidDirectionException(String message, Throwable cause) {
        super(message, cause);
    }
}

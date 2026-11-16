package com.amethyst.automata.machine.exception;

public class InvalidStateException extends AutomatonException {
    public InvalidStateException(String message) {
        super(message);
    }

    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }
}

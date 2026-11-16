package com.amethyst.automata.machine.exception;

public class AutomatonException extends RuntimeException {
    public AutomatonException(String message) {
        super(message);
    }

    public AutomatonException(String message, Throwable cause) {
        super(message, cause);
    }
}


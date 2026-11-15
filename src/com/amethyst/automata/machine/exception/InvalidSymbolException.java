package com.amethyst.automata.machine.exception;

public class InvalidSymbolException extends AutomatonException {
    public InvalidSymbolException(String message) {
        super(message);
    }

    public InvalidSymbolException(String message, Throwable cause) {
        super(message, cause);
    }
}

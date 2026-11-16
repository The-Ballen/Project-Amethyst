package com.amethyst.automata.machine.exception;

public class InvalidRuleException extends AutomatonException {
    public InvalidRuleException(String message) {
        super(message);
    }

    public InvalidRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}

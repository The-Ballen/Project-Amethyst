package com.amethyst.automata.machine;

import com.amethyst.automata.machine.exception.InvalidStateException;

public enum States {
    START("Start", false, false),
    
    // [...]
    
    REJECT("Reject", true, false),
    ACCEPT("Accept", false, true);

    private final String REPRESENTATION;
    private final boolean IS_REJECT;
    private final boolean IS_ACCEPT;

    private States(String REPRESENTATION, boolean IS_REJECT, boolean IS_ACCEPT) {
        this.REPRESENTATION = REPRESENTATION;
        this.IS_REJECT = IS_REJECT;
        this.IS_ACCEPT = IS_ACCEPT;

        if (IS_REJECT && IS_ACCEPT) {
            throw new InvalidStateException(
                "Um estado não pode ser de aceitação e rejeição simultaneamente!" +
                " (\'" + this.toString() + "\')"
            );
        }
    }

    public boolean isReject() {
        return IS_REJECT;
    }

    public boolean isAccept() {
        return IS_ACCEPT;
    }

    @Override
    public String toString() {
        return REPRESENTATION;
    }
}

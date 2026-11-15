package com.amethyst.automata.machine.alphabet;

import com.amethyst.automata.machine.exception.InvalidSymbolException;

public enum Symbol {
    X('X', false, true),
    
    // [...]

    BLANK('_', false, true);

    private final char REPRESENTATION;
    private final boolean IS_BLANK;
    private final boolean INPUT_SYMBOL;

    private Symbol(char REPRESENTATION, boolean IS_BLANK, boolean INPUT_SYMBOL) {
        this.REPRESENTATION = REPRESENTATION;
        this.IS_BLANK = IS_BLANK;
        this.INPUT_SYMBOL = INPUT_SYMBOL;

        if (IS_BLANK && INPUT_SYMBOL) {
            throw new InvalidSymbolException(
                "Um símbolo de entrada não pode ser branco!" +
                " (\'" + this.toString() + "\')"
            );
        }
    }

    public boolean isBlank() {
        return IS_BLANK;
    }

    public boolean isInput() {
        return INPUT_SYMBOL;
    }

    @Override
    public String toString() {
        return Character.toString(REPRESENTATION);
    }
}

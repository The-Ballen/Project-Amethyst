package com.amethyst.automata.machine.alphabet;

import java.util.HashMap;
import java.util.Map;

import com.amethyst.automata.machine.exception.InvalidSymbolException;

public enum Symbol {
    X('X', false, true),
    O('O', false, true),
    
    // [...]

    BLANK('_', true, false);

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

    public char getRepresentation() {
        return REPRESENTATION;
    }

    public boolean isBlank() {
        return IS_BLANK;
    }

    public boolean isInput() {
        return INPUT_SYMBOL;
    }

    // Cache estático para buscas
    private static final Map<Character, Symbol> LOOKUP_MAP = new HashMap<>();

    // Preenche o cache uma única vez ao carregar o enum na memória
    static {
        for (Symbol symbol : values()) {
            LOOKUP_MAP.put(symbol.getRepresentation(), symbol);
        }
    }

    public static Symbol fromRepresentation(char c) {
        return LOOKUP_MAP.get(c);
    }

    @Override
    public String toString() {
        return Character.toString(REPRESENTATION);
    }
}

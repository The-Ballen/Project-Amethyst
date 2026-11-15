package com.amethyst.automata.machine.transition.action;

import java.util.Objects;

import com.amethyst.automata.machine.States;
import com.amethyst.automata.machine.alphabet.Symbol;
import com.amethyst.automata.machine.tape.HeadDirection;

public record MultiTapeAction  (
    States nextState,
    Symbol[] writeSymbols,
    HeadDirection[] directions
) {
    public MultiTapeAction {
        Objects.requireNonNull(nextState, "O próximo estado não pode ser nulo.");

        // Verifica se o arranjo de símbolos é nulo
        Objects.requireNonNull(writeSymbols, "Os símbolos escritos não podem ser nulos.");

        // Verifica se algum dos símbolos do arranjo é nulo
        for (Symbol symbol : writeSymbols) {
            Objects.requireNonNull(symbol, "Os símbolos escritos não podem ser nulos.");
        }
        
        // Verifica se o arranjo de direções é nulo
        Objects.requireNonNull(directions, "As direções das cabeças das fitas não podem ser nulas.");

        // Verifica se alguma das direções do arranjo é nula
        for (HeadDirection direction : directions) {
            Objects.requireNonNull(direction, "As direções das cabeças das fitas não podem ser nulas.");
        }
    }
}

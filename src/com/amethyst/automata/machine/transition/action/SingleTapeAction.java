package com.amethyst.automata.machine.transition.action;

import java.util.Objects;

import com.amethyst.automata.machine.States;
import com.amethyst.automata.machine.alphabet.Symbol;
import com.amethyst.automata.machine.tape.HeadDirection;

public record SingleTapeAction (
    States nextState,
    Symbol writeSymbol,
    HeadDirection direction
) {
    public SingleTapeAction {
        Objects.requireNonNull(nextState, "O próximo estado não pode ser nulo.");
        Objects.requireNonNull(writeSymbol, "O símbolo escrito não pode ser nulo.");
        Objects.requireNonNull(direction, "A direção da cabeça da fita não pode ser nula.");
    }
}

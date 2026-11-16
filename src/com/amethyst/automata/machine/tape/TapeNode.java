package com.amethyst.automata.machine.tape;

import java.util.Arrays;
import java.util.Objects;

import com.amethyst.automata.machine.alphabet.Symbol;

class TapeNode {
    private final Symbol[] data;
    private TapeNode prev;
    private TapeNode next;
    final Symbol blankSymbol;
    final int size;

    TapeNode(TapeNode prev, TapeNode next, Symbol blankSymbol, int size) {
        this.prev = prev;
        this.next = next;
        this.blankSymbol = Objects.requireNonNull(blankSymbol, "O símbolo branco não pode ser nulo.");
        this.size = size;

        // Aloca o arranjo de símbolos
        data = new Symbol[size];

        // Inicializa o arranjo de símbolos com o símbolo branco
        Arrays.fill(this.data, blankSymbol);
    }

    TapeNode getNext() {
        return next;
    }

    TapeNode getPrev() {
        return prev;
    }

    void setNext(TapeNode next) {
        this.next = next;
    }

    void setPrev(TapeNode prev) {
        this.prev = prev;
    }

    Symbol read(int index) {
        return data[index];
    }

    void write(int index, Symbol symbol) {
        data[index] = Objects.requireNonNull(symbol, "O símbolo escrito na fita não pode ser nulo.");
    }
}

package com.amethyst.automata.machine;

import com.amethyst.automata.machine.alphabet.Symbol;
import com.amethyst.automata.machine.exception.InvalidSymbolException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Word {
    private final List<Symbol> symbols;

    public Word(List<Symbol> symbols) {
        Objects.requireNonNull(symbols, "A lista de símbolos não pode ser nula.");
        this.symbols = List.copyOf(symbols);
    }

    public Word(Symbol[] symbols) {
        Objects.requireNonNull(symbols, "O array de símbolos não pode ser nulo.");
        this.symbols = List.of(symbols);
    }

    public static Word fromString(String rawString) {
        Objects.requireNonNull(rawString, "A string de entrada não pode ser nula.");

        List<Symbol> parsed = new ArrayList<>(rawString.length());

        for (int i = 0; i < rawString.length(); i++) {
            char c = rawString.charAt(i);

            // Busca o símbolo no cache estático do enum
            Symbol matched = Symbol.fromRepresentation(c);
            
            if (matched == null) {
                throw new InvalidSymbolException(
                    "O símbolo '" + c + "' na posição " + i + " não pertence ao alfabeto da máquina."
                );
            }
            
            // Adiciona o símbolo encontrado na palavra
            parsed.add(matched);
        }

        return new Word(parsed);
    }

    public int length() {
        return symbols.size();
    }

    public boolean isEmpty() {
        return symbols.isEmpty();
    }

    public Symbol get(int index) {
        return symbols.get(index);
    }

    @Override
    public String toString() {
        if (symbols.isEmpty()) {
            return "λ";
        }

        StringBuilder sb = new StringBuilder(symbols.size());

        // Monta uma string com as representações de cada símbolo da palavra
        for (Symbol symbol : symbols) {
            sb.append(symbol.getRepresentation());
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        // Verifica se ambos apontam para o mesmo objeto na memória
        if (this == o) return true;
        
        // Verifica se o objeto é nulo ou se pertence a uma classe diferente
        if (!(o instanceof Word that)) return false;
        
        // Compara símbolo a símbolo das palavras
        return symbols.equals(that.symbols);
    }

    @Override
    public int hashCode() {
        return symbols.hashCode();
    }
}

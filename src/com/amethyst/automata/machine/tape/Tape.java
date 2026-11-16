package com.amethyst.automata.machine.tape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.amethyst.automata.machine.Word;
import com.amethyst.automata.machine.alphabet.Symbol;

public class Tape {
    public static final int NODE_SIZE = 100;

    private final TapeNode originNode;
    private final Symbol blankSymbol;

    public Tape(Symbol blankSymbol, Word initialWord) {
        this.blankSymbol = Objects.requireNonNull(blankSymbol, "O símbolo branco não pode ser nulo.");
        Objects.requireNonNull(initialWord, "A palavra inicial não pode ser nula.");

        // Primeiro nó da fita
        this.originNode = new TapeNode(null, null, blankSymbol, NODE_SIZE);

        // Cria uma cabeça inicial temporária somente para inicializar a fita
        TapeHead tempHead = new TapeHead(this);

        // Escreve a palavra de entrada na fita
        for (int i = 0; i < initialWord.length(); i++) {
            // Escreve o símbolo atual
            tempHead.write(initialWord.get(i));

            // Se já for o último símbolo, não movimentamos mais a cabeça da fita
            // (para evitar alocações extras de memória acidentais)
            if (i < initialWord.length() - 1) {
                tempHead.move(HeadDirection.RIGHT);
            }
        }
    }

    TapeNode getOriginNode() {
        return originNode;
    }

    Symbol getBlankSymbol() {
        return blankSymbol;
    }

    public Word toWord() {
        TapeNode node = originNode;
        List<Symbol> allSymbols = new ArrayList<>();

        // Encontra o primeiro nó à esquerda da lista
        while (node.getPrev() != null) {
            node = node.getPrev();
        }
        
        // Armazena todos os símbolos encontrados em cada posição de cada nó da lista
        while (node != null) {
            for (int i = 0; i < node.size; i++) {
                allSymbols.add(node.read(i));
            }
            node = node.getNext();
        }

        // Encontra o primeiro símbolo não-branco da fita
        int firstNonBlank = 0;
        while (firstNonBlank < allSymbols.size() && allSymbols.get(firstNonBlank).isBlank()) {
            firstNonBlank++;
        }

        // Se o índice encontrado estiver fora do arranjo, a fita está vazia (só tem símbolos brancos)
        if (firstNonBlank == allSymbols.size()) {
            return new Word(Collections.emptyList());
        }

        // Encontra o último símbolo não-branco da fita
        int lastNonBlank = allSymbols.size() - 1;
        while (lastNonBlank >= firstNonBlank && allSymbols.get(lastNonBlank).isBlank()) {
            lastNonBlank--;
        }

        // Retorna uma palavra formada por todos os símbolos encontrados
        // entre o primeiro e o último símbolo não-branco da fita
        return new Word(allSymbols.subList(firstNonBlank, lastNonBlank + 1));
    }
}

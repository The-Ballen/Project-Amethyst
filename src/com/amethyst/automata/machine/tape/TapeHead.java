package com.amethyst.automata.machine.tape;

import java.util.Objects;

import com.amethyst.automata.machine.alphabet.Symbol;

public class TapeHead {
    private TapeNode currentNode;
    private int position;

    public TapeHead(Tape tape) {
        Objects.requireNonNull(tape, "A fita não pode ser nula.");
        this.currentNode = tape.getOriginNode();
        this.position = 0;
    }

    public Symbol read() {
        return currentNode.read(position);
    }

    public void write(Symbol symbol) {
        currentNode.write(position, symbol);
    }

    public void move(HeadDirection direction) {
        Objects.requireNonNull(direction, "A direção do movimento da cabeça da fita não pode ser nula.");

        switch (direction) {
            case LEFT -> {
                // Testa se já estamos no limite esquerdo do nó
                if (position == 0) {
                    // Testa se existe um nó à esquerda
                    if (currentNode.getPrev() == null) {
                        // Aloca um novo nó à esquerda
                        currentNode.setPrev(
                            new TapeNode(
                                null,
                                currentNode,
                                currentNode.blankSymbol,
                                currentNode.size
                            )
                        );
                    }

                    // Move a cabeça da fita para a última posição do nó à esquerda
                    currentNode = currentNode.getPrev();
                    position = currentNode.size - 1;
                } else {
                    position--;
                }
            }
            case STAY -> {
                // Cabeçote permanece na mesma posição
            }
            case RIGHT -> {
                // Testa se já estamos no limite direito do nó
                if (position == currentNode.size - 1) {
                    // Testa se existe um nó à esquerda
                    if (currentNode.getNext() == null) {
                        // Aloca um novo nó à direita
                        currentNode.setNext(
                            new TapeNode(
                                currentNode,
                                null,
                                currentNode.blankSymbol,
                                currentNode.size
                            )
                        );
                    }

                    // Move a cabeça da fita para a primeira posição do nó à direita
                    currentNode = currentNode.getNext();
                    position = 0;
                } else {
                    position++;
                }
            }
        }
    }
}

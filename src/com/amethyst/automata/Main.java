package com.amethyst.automata;

import java.util.List;

import com.amethyst.automata.machine.ComputationResult;
import com.amethyst.automata.machine.State;
import com.amethyst.automata.machine.TuringMachine;
import com.amethyst.automata.machine.alphabet.Symbol;
import com.amethyst.automata.machine.tape.HeadDirection;
import com.amethyst.automata.machine.transition.TransitionFunction;

public class Main {
    public static void main(String[] args) {
        // Cria uma função de transição
        TransitionFunction delta = new TransitionFunction();

        // Exemplo rápido para testes: Máquina que procura o primeiro 'X', troca por 'O' e aceita.
        // Se chegar no fim da fita sem achar um 'X', rejeita.
        delta.addRule(State.START, Symbol.O, State.START, Symbol.O, HeadDirection.RIGHT)
             .addRule(State.START, Symbol.X, State.ACCEPT, Symbol.O, HeadDirection.STAY)
             .addRule(State.START, Symbol.BLANK, State.REJECT, Symbol.BLANK, HeadDirection.STAY);

        // Instancia uma máquina com essas regras
        TuringMachine machine = new TuringMachine(delta);

        // Cria uma lista de palavras de entrada (para realizar os testes)
        List<String> testInputs = List.of(
            "X",
            "OXO",
            "OOX",
            "OOO",
            "" // A palavra vazia
        );

        System.out.println("Total de regras carregadas: " + delta.totalRules() + "\n");

        for (String rawInput : testInputs) {
            // Insere a palavra na máquina e armazena o resultado da computação
            ComputationResult result = machine.compute(rawInput);

            System.out.printf(
                "Entrada: \"%s\" | Veredito: %s | Fita Final: \"%s\" | Passos: %d%n",
                rawInput.isEmpty() ? "λ" : rawInput, // Se for a palavra vazia, imprime um lambda
                result.verdict(),
                result.finalTapeWord(),
                result.stepsExecuted()
            );
        }
    }
}

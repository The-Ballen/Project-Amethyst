package com.amethyst.automata.machine.transition;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.amethyst.automata.machine.State;
import com.amethyst.automata.machine.alphabet.Symbol;
import com.amethyst.automata.machine.tape.HeadDirection;
import com.amethyst.automata.machine.transition.action.SingleTapeAction;

public class TransitionFunction {
    // Mapa externo: Estado Atual -> Mapa interno
    // Mapa interno: Símbolo Lido -> Ação
    private final Map<State, Map<Symbol, SingleTapeAction>> delta;

    public TransitionFunction() {
        this.delta = new HashMap<>(); // Mapa que armazenará as regras de transições
    }

    /**
     * Registra uma regra: δ(currentState, readSymbol) = (nextState, writeSymbol, direction)
     */
    public TransitionFunction addRule(
            State currentState,
            Symbol readSymbol,
            State nextState,
            Symbol writeSymbol,
            HeadDirection direction) {

        Objects.requireNonNull(currentState, "O estado atual não pode ser nulo.");
        Objects.requireNonNull(readSymbol, "O símbolo lido não pode ser nulo.");

        SingleTapeAction action = new SingleTapeAction(nextState, writeSymbol, direction);

        // Cria um EnumMap para os símbolos daquele estado, se ainda não existir
        delta.computeIfAbsent(currentState, k -> new EnumMap<>(Symbol.class))
             .put(readSymbol, action);

        // Retornamos o próprio objeto para permitir o encadeamento de chamadas de métodos
        return this;
    }

    /**
     * Executa a transição. Retorna Optional.empty() se não houver transição definida.
     */
    public Optional<SingleTapeAction> apply(State currentState, Symbol readSymbol) {
        // Resgata o mapa de regras para o estado atual (Símbolo -> Ação)
        Map<Symbol, SingleTapeAction> stateRules = delta.get(currentState);

        // Se o mapa de regras for nulo, não existem transições definidas para esse estado
        if (stateRules == null) {
            return Optional.empty();
        }

        // Procura e retorna a regra para o símbolo lido (ou `Optional.empty()`, caso não existir)
        return Optional.ofNullable(stateRules.get(readSymbol));
    }

    /**
     * Utilitário para saber quantas regras totais foram carregadas na máquina.
     */
    public int totalRules() {
        // `delta.values()` retorna todos os os mapas internos (Símbolo -> Ação).
        // `.stream()` permite realizar operações individuais em cada mapa.
        // `.mapToInt(Map::size)` calcula quantos elementos (regras) existem no mapa atual.
        // `.sum()` soma todos o tamanhos de todos os mapas em um único inteiro e retorna.
        return delta.values().stream().mapToInt(Map::size).sum();
    }
}

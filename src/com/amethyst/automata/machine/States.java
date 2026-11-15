package com.amethyst.automata.machine;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.amethyst.automata.machine.exception.InvalidStateException;

public final class States {
    // Contador automático para IDs únicos (0, 1, 2...) de cada estado
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    // Pool de estados para garantir que nunca existam dois objetos para o mesmo estado na memória
    private static final Map<String, States> STATE_POOL = new ConcurrentHashMap<>();

    // Estados básicos
    public static final States START  = register("START", false, false);
    public static final States REJECT = register("REJECT", true, false);
    public static final States ACCEPT = register("ACCEPT", false, true);

    private final int ID;
    private final String REPRESENTATION;
    private final boolean IS_REJECT;
    private final boolean IS_ACCEPT;

    private States(String REPRESENTATION, boolean IS_REJECT, boolean IS_ACCEPT) {
        this.ID = ID_GENERATOR.getAndIncrement(); // Gera um ID único para esse estado
        this.REPRESENTATION = REPRESENTATION;
        this.IS_REJECT = IS_REJECT;
        this.IS_ACCEPT = IS_ACCEPT;

        if (IS_REJECT && IS_ACCEPT) {
            throw new InvalidStateException(
                "Um estado não pode ser de aceitação e rejeição simultaneamente!" +
                " (\'" + this.toString() + "\')"
            );
        }
    }

    /**
     * Cria ou recupera um estado intermediário (não-terminal) pelo nome.
     */
    public static States of(String name) {
        return register(name, false, false);
    }

    /**
     * Registra ou recupera um estado configurando se é de rejeição ou aceitação.
     */
    public static States register(String name, boolean isReject, boolean isAccept) {
        Objects.requireNonNull(name, "O nome do estado não pode ser nulo.");

        // Se já existir, retorna; senão, cria o estado novo e retorna sua instância
        return STATE_POOL.computeIfAbsent(
            name.trim(),
            key -> new States(key, isReject, isAccept)
        );
    }

    public int id() {
        return ID;
    }

    public boolean isReject() {
        return IS_REJECT;
    }

    public boolean isAccept() {
        return IS_ACCEPT;
    }

    public boolean isHalting() {
        return IS_REJECT || IS_ACCEPT;
    }

    @Override
    public String toString() {
        return REPRESENTATION;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ID);
    }
}

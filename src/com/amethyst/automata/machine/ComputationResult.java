package com.amethyst.automata.machine;

public record ComputationResult (
    Verdict verdict,
    Word finalTapeWord,
    long stepsExecuted
) {}

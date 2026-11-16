package com.amethyst.automata.machine;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import com.amethyst.automata.machine.alphabet.Symbol;
import com.amethyst.automata.machine.exception.AutomatonException;
import com.amethyst.automata.machine.exception.InvalidSymbolException;
import com.amethyst.automata.machine.tape.Tape;
import com.amethyst.automata.machine.tape.TapeHead;
import com.amethyst.automata.machine.transition.TransitionFunction;
import com.amethyst.automata.machine.transition.action.SingleTapeAction;
import com.amethyst.automata.machine.transition.rules.RuleLoader;

public class TuringMachine {
    // Limite padrão para evitar loops infinitos (1 milhão de passos)
    public static final long DEFAULT_MAX_STEPS = 1_000_000L;

    private final TransitionFunction DELTA;
    private final State INITIAL_STATE;
    private final Symbol BLANK_SYMBOL;
    private final long MAX_STEPS;

    /**
     * Construtor completo da Máquina de Turing.
     */
    public TuringMachine(TransitionFunction DELTA, State INITIAL_STATE, Symbol BLANK_SYMBOL, long MAX_STEPS) {
        this.DELTA = Objects.requireNonNull(DELTA, "A função de transição não pode ser nula.");
        this.INITIAL_STATE = Objects.requireNonNull(INITIAL_STATE, "O estado inicial não pode ser nulo.");
        this.BLANK_SYMBOL = Objects.requireNonNull(BLANK_SYMBOL, "O símbolo branco não pode ser nulo.");

        if (!BLANK_SYMBOL.isBlank()) {
            throw new InvalidSymbolException("O símbolo branco deve ser definido como branco.");
        }

        if (MAX_STEPS <= 0) {
            throw new IllegalArgumentException("O limite máximo de passos da máquina deve ser maior que zero.");
        }

        this.MAX_STEPS = MAX_STEPS;
    }

    public TuringMachine(TransitionFunction DELTA) {
        this(DELTA, State.START, Symbol.BLANK, DEFAULT_MAX_STEPS);
    }

    public TuringMachine(TransitionFunction DELTA, State INITIAL_STATE, Symbol BLANK_SYMBOL) {
        this(DELTA, INITIAL_STATE, BLANK_SYMBOL, DEFAULT_MAX_STEPS);
    }

    public TuringMachine(TransitionFunction DELTA, State INITIAL_STATE) {
        this(DELTA, INITIAL_STATE, Symbol.BLANK, DEFAULT_MAX_STEPS);
    }

    /**
     * Método para criar uma máquina carregando as regras de transição de um arquivo.
     */
    public static TuringMachine fromFile(Path rulesFilePath) {
        // Instancia uma função de transição
        TransitionFunction DELTA = new TransitionFunction();

        // Carrega as regras do arquivo e popula a função de transição
        RuleLoader.loadFromFile(rulesFilePath, DELTA);
        
        // Retorna uma Máquina de Turing instanciada com a função de transição gerada
        return new TuringMachine(DELTA);
    }

    /**
     * Computa uma String de entrada.
     */
    public ComputationResult compute(String rawInput) {
        return compute(Word.fromString(rawInput));
    }

    /**
     * Executa a computação da máquina sobre uma palavra de entrada.
     */
    public ComputationResult compute(Word inputWord) {
        Objects.requireNonNull(inputWord, "A palavra de entrada não pode ser nula.");

        // Valida se a palavra pertence ao alfabeto de entrada
        validateInputAlphabet(inputWord);

        // Inicializa a fita com a palavra de entrada
        Tape tape = new Tape(BLANK_SYMBOL, inputWord);

        // Instancia um cabeçote nessa fita (posicionado no primeiro símbolo da palavra, caso seja não seja vazia)
        TapeHead head = new TapeHead(tape);

        // Armazena o estado atual da máquina (inicializado com o estado inicial)
        State currentState = INITIAL_STATE;

        // Armazena a quantidade de passos executados pela máquina
        long steps = 0L;

        // Executa enquanto o estado atual não for um estado de parada
        while (!currentState.isHalting()) {
            // Verifica se a máquina já bateu o limite de passos definido
            if (steps >= MAX_STEPS) {
                throw new AutomatonException(
                    "Limite máximo de passos (\"" + MAX_STEPS + "\") atingido no estado " + currentState
                );
            }

            // Lê o símbolo na posição atual da cabeça da fita
            Symbol readSymbol = head.read();

            // Chama a função de transição para o estado atual e símbolo lido e armazena o resultado
            Optional<SingleTapeAction> actionOpt = DELTA.apply(currentState, readSymbol);

            // Se a transição for indefinida, a máquina para e rejeita implicitamente
            if (actionOpt.isEmpty()) {
                return new ComputationResult(Verdict.REJECT, tape.toWord(), steps);
            }

            // Lê a ação retornada pela função de transição
            SingleTapeAction action = actionOpt.get();

            // Escreve na cabeça da fita o símbolo retornado pela função de transição
            head.write(action.writeSymbol());

            // Move a cabeça da fita na posição retornada pela função de transição
            head.move(action.direction());

            // Transiciona a máquina para o estado retornado pela função de transição
            currentState = action.nextState();

            // Incrementa o contador de passos realizados pela máquina
            steps++;
        }

        // Define o veredito com base no estado de parada (ACCEPT ou REJECT)
        Verdict verdict = currentState.isAccept() ? Verdict.ACCEPT : Verdict.REJECT;
        return new ComputationResult(verdict, tape.toWord(), steps);
    }

    /**
     * Valida se uma palavra pertence ao Fecho de Kleene do alfabeto de entrada da máquina.
     */
    private void validateInputAlphabet(Word inputWord) {
        // Executa para cada símbolo da palavra
        for (int i = 0; i < inputWord.length(); i++) {
            Symbol s = inputWord.get(i);

            // Se o símbolo for de entrada, não pertence ao Fecho de Kleene do alfabeto de entrada
            if (!s.isInput()) {
                throw new InvalidSymbolException(
                    "O símbolo '" + s + "' na posição " + i +
                    " pertence ao alfabeto da fita, mas não ao alfabeto de entrada."
                );
            }
        }
    }

    public TransitionFunction getTransitionFunction() {
        return DELTA;
    }
}

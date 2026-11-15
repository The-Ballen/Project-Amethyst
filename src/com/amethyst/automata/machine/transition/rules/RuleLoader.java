package com.amethyst.automata.machine.transition.rules;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amethyst.automata.machine.States;
import com.amethyst.automata.machine.alphabet.Symbol;
import com.amethyst.automata.machine.exception.AutomatonException;
import com.amethyst.automata.machine.exception.InvalidDirectionException;
import com.amethyst.automata.machine.exception.InvalidRuleException;
import com.amethyst.automata.machine.exception.InvalidSymbolException;
import com.amethyst.automata.machine.tape.HeadDirection;
import com.amethyst.automata.machine.transition.TransitionFunction;


public final class RuleLoader {
    // Expressão regular no formato: δ(q, a) = (p, b, D)
    private static final Pattern FORMAL_RULE_PATTERN = Pattern.compile(
        "^(?:δ|delta)?\\s*\\(\\s*([^,]+?)\\s*,\\s*(.)\\s*\\)\\s*=\\s*\\(\\s*([^,]+?)\\s*,\\s*(.)\\s*,\\s*([A-Za-z]+)\\s*\\)$"
    );

    /**
     * Não instancie essa classe.
     */
    private RuleLoader() {}

    public static void loadFromFile(Path filePath, TransitionFunction delta) {
        // UTF_8 garante que o caractere 'δ' seja lido corretamente
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Ignora linhas vazias e comentários (iniciados com // ou #)
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                    continue;
                }

                // Motor para expressões regulares
                Matcher matcher = FORMAL_RULE_PATTERN.matcher(line);

                // Aplica a expressão regular na linha atual e verifica se ela é inválida
                if (!matcher.matches()) {
                    throw new InvalidRuleException(
                        "Erro de sintaxe na linha " + lineNumber + ": \"" + line + "\"" +
                        "\nFormato esperado: δ(estadoAtual, simboloLido) = (proximoEstado, simboloEscrito, L|S|R)"
                    );
                }

                // Extrai os 5 elementos dos grupos da expressão regular
                States currentState = States.of(matcher.group(1));
                Symbol readSymbol   = parseSymbol(matcher.group(2).charAt(0), lineNumber);
                States nextState    = States.of(matcher.group(3)); // Se o estado não já existir, cria ele agora
                Symbol writeSymbol  = parseSymbol(matcher.group(4).charAt(0), lineNumber);
                HeadDirection dir   = parseDirection(matcher.group(5), lineNumber);

                // Registra a regra na função de transição
                delta.addRule(currentState, readSymbol, nextState, writeSymbol, dir);
            }
        } catch (IOException e) {
            throw new AutomatonException("Erro ao abrir o arquivo de transições: " + filePath, e);
        }
    }

    private static Symbol parseSymbol(char c, int lineNumber) {
        Symbol symbol = Symbol.fromRepresentation(c);

        if (symbol == null) {
            throw new InvalidSymbolException(
                "O símbolo '" + c + "' na linha " + lineNumber + " não existe no alfabeto \"Symbol\"."
            );
        }

        return symbol;
    }

    private static HeadDirection parseDirection(String rawDir, int lineNumber) {
        return switch (rawDir.toUpperCase()) {
            case "L", "LEFT"  -> HeadDirection.LEFT;
            case "S", "STAY"  -> HeadDirection.STAY;
            case "R", "RIGHT" -> HeadDirection.RIGHT;
            default -> throw new InvalidDirectionException(
                "A direção \"" + rawDir + "\" na linha " + lineNumber +
                " é inválida. Use L | S | R (ou LEFT | STAY | RIGHT)."
            );
        };
    }
}

package ru.nsu.kiryushin;

import java.util.Scanner;

/**
 * Entry point for the expression parsing demo.
 */
public class Main {

    /**
     * Parses an expression from standard input, prints it, its derivative and evaluates it.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        String derivativeVariable = args.length > 0 ? args[0] : "x";
        System.out.println("Enter expression");
        try (Scanner scanner = new Scanner(System.in)) {
            if (!scanner.hasNextLine()) {
                System.err.println("No expression provided.");
                return;
            }
            String expressionText = scanner.nextLine();
            System.out.println("Enter which variables to define");
            String assignments = scanner.hasNextLine() ? scanner.nextLine() : "";

            Parser parser = new Parser(expressionText);
            Expression expression = parser.parse();

            System.out.println(expression);
            System.out.println(expression.simplification());
            System.out.println(expression.derivative(derivativeVariable));
            try {
                System.out.println(expression.eval(assignments));
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}

package com.xchess.stockfish.validators;

public class SquareValidator {
    private SquareValidator() {
    }

    public static boolean isSquareSyntaxValid(String square) {
        return square.matches("^[a-h][1-8]$");
    }
}

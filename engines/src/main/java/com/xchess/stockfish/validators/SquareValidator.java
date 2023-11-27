package com.xchess.stockfish.validators;

public class SquareValidator {
    private SquareValidator() {
    }

    public static boolean isSquareSyntaxValid(String square) {
        return square.length() == 2 && square.matches("^[a-h][1-8]$");
    }
}

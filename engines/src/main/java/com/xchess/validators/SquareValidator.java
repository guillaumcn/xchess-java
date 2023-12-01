package com.xchess.validators;

/**
 * Validator for square syntax
 */
public class SquareValidator {
    private SquareValidator() {
    }

    /**
     * @param square The square to check
     * @return true if the square is valid
     */
    public static boolean isSquareSyntaxValid(String square) {
        return square.matches("^[a-h][1-8]$");
    }
}

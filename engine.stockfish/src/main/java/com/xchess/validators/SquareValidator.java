package com.xchess.validators;

public class SquareValidator {
    public static boolean isSquareSyntaxValid(String square) {
        return square.length() == 2 && square.matches("^[a-h][0-9]$");
    }
}

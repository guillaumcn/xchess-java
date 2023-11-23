package com.xchess.validators;

public class MoveValidator {
    public static boolean isMoveValid(String move) {
        return move.length() == 4 &&
                SquareValidator.isSquareSyntaxValid(move.substring(0, 2)) &&
                SquareValidator.isSquareSyntaxValid(move.substring(2));
    }
}

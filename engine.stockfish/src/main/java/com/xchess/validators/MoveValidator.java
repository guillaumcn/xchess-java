package com.xchess.validators;

import java.util.Arrays;

public class MoveValidator {
    public static boolean isMoveValid(String move) {
        if (move.length() == 4) {
            return SquareValidator.isSquareSyntaxValid(move.substring(0, 2)) &&
                    SquareValidator.isSquareSyntaxValid(move.substring(2));
        }

        if (move.length() == 5) {
            return MoveValidator.isMoveValid(move.substring(0, 4)) && Arrays.asList("q", "r", "b", "n").contains(move.substring(4));
        }

        return false;
    }
}

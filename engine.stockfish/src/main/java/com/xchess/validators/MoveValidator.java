package com.xchess.validators;

public class MoveValidator {

    private MoveValidator() {
    }

    public static boolean isMoveValid(String move) {
        if (move.length() == 4) {
            return SquareValidator.isSquareSyntaxValid(move.substring(0, 2)) &&
                    SquareValidator.isSquareSyntaxValid(move.substring(2));
        }

        if (move.length() == 5) {
            return MoveValidator.isMoveValid(move.substring(0, 4)) && PieceValidator.isPromotablePieceValid(move.substring(4));
        }

        return false;
    }
}

package com.xchess.validators;

import java.util.Arrays;

/**
 * Validator for piece syntax
 */
public class PieceValidator {
    private PieceValidator() {
    }

    /**
     * @param piece The piece to check
     * @return true if the piece is valid
     */
    public static boolean isPieceValid(String piece) {
        return Arrays.asList("q", "r", "b", "n", "p",
                "k").contains(piece.toLowerCase());
    }

    /**
     * @param piece The piece to check
     * @return true if the piece is a promotable piece
     */
    public static boolean isPromotablePieceValid(String piece) {
        return Arrays.asList("q", "r", "b", "n").contains(piece.toLowerCase());
    }
}

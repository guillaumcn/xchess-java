package com.xchess.validators;

import java.util.Arrays;

public class PieceValidator {
    private PieceValidator() {
    }

    public static boolean isPieceValid(String piece) {
        return Arrays.asList("q", "r", "b", "n", "p",
                "k").contains(piece.toLowerCase());
    }

    public static boolean isPromotablePieceValid(String piece) {
        return Arrays.asList("q", "r", "b", "n").contains(piece.toLowerCase());
    }
}

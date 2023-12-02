package com.xchess.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PieceValidatorTest {
    @Test
    public void shouldReturnTrueIfNormalPiece() {
        assertTrue(PieceValidator.isPieceValid("p"));
    }

    @Test
    public void shouldReturnTrueIfPromotablePiece() {
        assertTrue(PieceValidator.isPromotablePieceValid("r"));
    }

    @Test
    public void shouldReturnFalseIfNotPromotablePiece() {
        assertFalse(PieceValidator.isPromotablePieceValid("p"));
    }

    @Test
    public void shouldReturnFalseIfPieceNotValid() {
        assertFalse(PieceValidator.isPromotablePieceValid("a"));
    }
}

package com.xchess.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FenSyntaxValidatorTest {
    @Test
    public void testShouldValidateIfValidBlackTurn() {
        assertTrue(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR b KQkq - 0 1"));
    }

    @Test
    public void testShouldValidateIfValidWhiteTurn() {
        assertTrue(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR w KQkq - 0 1"));
    }

    @Test
    public void testShouldNotValidateIfInvalidBlackOrWhiteTurn() {
        assertFalse(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR g KQkq - 0 1"));
    }

    @Test
    public void testShouldValidateIfValidCastleInformation() {
        assertTrue(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR b Kq - 0 1"));
    }

    @Test
    public void testShouldValidateIfNoOneCanCastle() {
        assertTrue(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR b - - 0 1"));
    }

    @Test
    public void testShouldNotValidateIfInvalidCastleInformation() {
        assertFalse(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR b Kekq - 0 1"));
    }

    @Test
    public void testShouldValidateIfSquareEnPassant() {
        assertTrue(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR b KQkq a1 0 1"));
    }

    @Test
    public void testShouldNotValidateIfInvalidEnPassant() {
        assertFalse(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR b KQkq invalidenpassant 0 1"));
    }

    @Test
    public void testShouldNotValidateIfInvalidMovesCount() {
        assertFalse(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP1P/RNBQKBNR b KQkq - invalid moves"));
    }

    @Test
    public void testShouldNotValidateIfNot8Lines() {
        assertFalse(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/RNBQKBNR b KQkq - 0 1"));
    }

    @Test
    public void testShouldNotValidateIfInvalidDigit() {
        assertFalse(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/9/PPPPPP1P/RNBQKBNR b KQkq - 0 1"));
    }

    @Test
    public void testShouldValidateIf2DigitFollowedOnOneLine() {
        assertFalse(FenSyntaxValidator.isFenSyntaxValid("rnbqkbnr/pppppppp/8" +
                "/8" +
                "/6P1/8/PPPPPP12P/RNBQKBNR b KQkq - 0 1"));
    }
}

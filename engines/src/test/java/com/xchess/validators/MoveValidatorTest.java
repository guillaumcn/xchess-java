package com.xchess.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MoveValidatorTest {
    @Test
    public void testShouldReturnTrueIfNormalMove() {
        assertTrue(MoveValidator.isMoveValid("e2e4"));
    }

    @Test
    public void testShouldReturnTrueIfNormalMoveWithPromotion() {
        assertTrue(MoveValidator.isMoveValid("e7e8r"));
    }

    @Test
    public void testShouldReturnFalseIfNormalMoveWithInvalidPromotion() {
        assertFalse(MoveValidator.isMoveValid("e7e8a"));
    }

    @Test
    public void testShouldReturnFalseIfInvalidMoveWithNormalPromotion() {
        assertFalse(MoveValidator.isMoveValid("i8e8r"));
    }

    @Test
    public void testShouldReturnFalseIfInvalidSourceSquare() {
        assertFalse(MoveValidator.isMoveValid("i8a4"));
    }

    @Test
    public void testShouldReturnFalseIfInvalidDestinationSquare() {
        assertFalse(MoveValidator.isMoveValid("a2i8"));
    }

    @Test
    public void testShouldReturnFalseIfInvalidMoveSize() {
        assertFalse(MoveValidator.isMoveValid("a2i"));
    }
}

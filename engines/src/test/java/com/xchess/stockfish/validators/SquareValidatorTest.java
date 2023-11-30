package com.xchess.stockfish.validators;

import com.xchess.validators.SquareValidator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SquareValidatorTest {
    @Test
    public void shouldReturnTrueIfSquareIsValid() {
        assertTrue(SquareValidator.isSquareSyntaxValid("a4"));
    }

    @Test
    public void shouldReturnFalseIfSquareIsNotValid() {
        assertFalse(SquareValidator.isSquareSyntaxValid("q9"));
    }
}

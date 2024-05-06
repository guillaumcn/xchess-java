package com.xchess.exceptions;

public class InvalidMoveSyntaxException extends Exception {
    public InvalidMoveSyntaxException(String move) {
        super("Invalid syntax for move " + move);
    }
}

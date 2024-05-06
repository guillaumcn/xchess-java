package com.xchess.exceptions;

public class InvalidSquareSyntaxException extends Exception {
    public InvalidSquareSyntaxException(String square) {
        super("Invalid syntax for square " + square);
    }
}

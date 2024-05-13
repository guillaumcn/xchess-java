package com.xchess.exceptions;

public class InvalidFenPositionException extends Exception {
    public InvalidFenPositionException(String fen) {
        super("Invalid fen position : " + fen);
    }
}

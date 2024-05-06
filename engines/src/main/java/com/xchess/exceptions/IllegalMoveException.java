package com.xchess.exceptions;

public class IllegalMoveException extends Exception {
    public IllegalMoveException(String move, String fen) {
        super("Illegal move " + move + " from position " + fen);
    }
}

package com.xchess.exceptions;

import java.io.IOException;

public class ProcessKilledException extends IOException {
    public ProcessKilledException() {
        super("Process has been killed");
    }
}

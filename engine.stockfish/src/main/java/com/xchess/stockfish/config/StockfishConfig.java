package com.xchess.stockfish.config;

import com.xchess.stockfish.constants.Constants;

public class StockfishConfig {
    private int timeoutInMs;

    public StockfishConfig() {
        this.timeoutInMs = Constants.DEFAULT_TIMEOUT;
    }

    public int getTimeoutInMs() {
        return timeoutInMs;
    }

    public StockfishConfig setTimeoutInMs(int timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
        return this;
    }
}

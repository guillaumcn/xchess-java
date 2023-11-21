package com.xchess.config;

import com.xchess.constants.Constants;

public class StockfishConfig {
    private int readTimeoutInMs;

    public StockfishConfig() {
        this.readTimeoutInMs = Constants.DEFAULT_READ_TIMEOUT;
    }

    public int getReadTimeoutInMs() {
        return readTimeoutInMs;
    }

    public StockfishConfig setReadTimeoutInMs(int readTimeoutInMs) {
        this.readTimeoutInMs = readTimeoutInMs;
        return this;
    }
}

package com.xchess.stockfish.config;

import com.xchess.stockfish.constants.Constants;
import lombok.Getter;

/**
 * Class for configuration of Stockfish engine integration
 */
@Getter
public class StockfishConfig {
    /**
     * -- GETTER --
     * Get the timeout for all readings actions on the Stockfish process
     *
     * @return current config value of timeout
     */
    private int timeoutInMs;

    public StockfishConfig() {
        this.timeoutInMs = Constants.DEFAULT_TIMEOUT;
    }

    /**
     * Set the timeout for all readings actions on the Stockfish process
     *
     * @param timeoutInMs the config to set for timeout in milliseconds
     * @return the config object
     */
    public StockfishConfig setTimeoutInMs(int timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
        return this;
    }
}

package com.xchess.stockfish.option;

public abstract class StockfishOption implements StockfishOptionInterface {
    protected StockfishOptionKey optionKey;

    protected StockfishOption(StockfishOptionKey name) {
        this.optionKey = name;
    }

    public StockfishOptionKey getOptionKey() {
        return optionKey;
    }

    public void setOptionKey(StockfishOptionKey optionKey) {
        this.optionKey = optionKey;
    }
}

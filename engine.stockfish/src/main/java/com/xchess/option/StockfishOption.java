package com.xchess.option;

public abstract class StockfishOption implements StockfishOptionInterface {
    protected StockfishOptionKey optionKey;

    public StockfishOption(StockfishOptionKey name) {
        this.optionKey = name;
    }

    public StockfishOptionKey getOptionKey() {
        return optionKey;
    }

    public void setOptionKey(StockfishOptionKey optionKey) {
        this.optionKey = optionKey;
    }
}

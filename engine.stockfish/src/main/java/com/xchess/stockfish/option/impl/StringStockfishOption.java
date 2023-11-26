package com.xchess.stockfish.option.impl;

import com.xchess.stockfish.option.StockfishOption;
import com.xchess.stockfish.option.StockfishOptionKey;

public class StringStockfishOption extends StockfishOption {
    protected String defaultValue;

    public StringStockfishOption(StockfishOptionKey key, String defaultValue) {
        super(key);
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void validate(Object optionValue) {
        // No validation required
    }
}

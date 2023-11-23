package com.xchess.option.impl;

import com.xchess.option.StockfishOption;
import com.xchess.option.StockfishOptionKey;

public class BooleanStockfishOption extends StockfishOption {
    protected boolean defaultValue;

    public BooleanStockfishOption(StockfishOptionKey key,
                                  boolean defaultValue) {
        super(key);
        this.defaultValue = defaultValue;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void validate(Object optionValue) {
        if (!(optionValue instanceof Boolean)) {
            String stringValue = optionValue.toString();
            if (!"true".equals(stringValue) && !"false".equals(stringValue)) {
                throw new IllegalArgumentException("Parameter " + optionKey.getValue() + " must be true or false");
            }
        }
    }
}

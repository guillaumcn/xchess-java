package com.xchess.option.impl;

import com.xchess.option.StockfishOption;
import com.xchess.option.StockfishOptionKey;

public class NumberStockfishOption extends StockfishOption {
    private int min;
    private int max;
    private int defaultValue;

    public NumberStockfishOption(StockfishOptionKey key, int defaultValue,
                                 int min, int max) {
        super(key);
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void validate(Object optionValue) {
        int integerValue = Integer.parseInt(optionValue.toString());
        if (integerValue < min) {
            throw new IllegalArgumentException("Parameter " + optionKey.getValue() + " should have a value greater than " + min);
        }
        if (integerValue > max) {
            throw new IllegalArgumentException("Parameter " + optionKey.getValue() + " should have a value lower than " + max);
        }
    }
}

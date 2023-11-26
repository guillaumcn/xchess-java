package com.xchess.stockfish.option;

public interface StockfishOptionInterface {
    void validate(Object optionValue);

    Object getDefaultValue();
}

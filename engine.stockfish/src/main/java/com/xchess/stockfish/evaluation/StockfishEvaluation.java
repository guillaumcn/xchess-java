package com.xchess.stockfish.evaluation;

import java.util.Objects;

public class StockfishEvaluation {
    private StockfishEvaluationType type;
    private int value;

    public StockfishEvaluation(StockfishEvaluationType type, int value) {
        this.type = type;
        this.value = value;
    }

    public StockfishEvaluationType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockfishEvaluation that = (StockfishEvaluation) o;
        return value == that.value && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}

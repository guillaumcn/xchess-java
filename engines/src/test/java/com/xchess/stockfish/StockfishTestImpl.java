package com.xchess.stockfish;

import com.xchess.process.ProcessWrapper;
import com.xchess.stockfish.config.StockfishConfig;
import com.xchess.stockfish.option.StockfishOptions;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class StockfishTestImpl extends Stockfish {
    private List<String> successiveFens;
    private int fenCallCount;
    private List<List<String>> successivePossibleMoves;
    private int possibleMovesCallCount;

    public StockfishTestImpl(ProcessWrapper process, StockfishConfig config) throws IOException, TimeoutException {
        super(process, config);
        this.fenCallCount = 0;
        this.possibleMovesCallCount = 0;
    }

    public void setSuccessiveFens(List<String> successiveFens) {
        this.successiveFens = successiveFens;
    }

    public void setSuccessivePossibleMoves(List<List<String>> successivePossibleMoves) {
        this.successivePossibleMoves = successivePossibleMoves;
    }

    @Override
    public String getFenPosition() throws IOException, TimeoutException {
        if (Objects.isNull(this.successiveFens) || this.successiveFens.size() <= fenCallCount) {
            return super.getFenPosition();
        } else {
            String result = this.successiveFens.get(fenCallCount);
            fenCallCount++;
            return result;
        }
    }

    @Override
    public List<String> getPossibleMoves() throws IOException, TimeoutException {
        if (Objects.isNull(this.successivePossibleMoves) || this.successivePossibleMoves.size() <= possibleMovesCallCount) {
            return super.getPossibleMoves();
        } else {
            List<String> result = this.successivePossibleMoves.get(possibleMovesCallCount);
            possibleMovesCallCount++;
            return result;
        }
    }
}

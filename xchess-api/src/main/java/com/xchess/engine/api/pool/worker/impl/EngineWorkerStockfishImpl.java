package com.xchess.engine.api.pool.worker.impl;

import com.xchess.Stockfish;
import com.xchess.config.StockfishConfig;
import com.xchess.engine.api.pool.worker.EngineWorker;
import com.xchess.process.ProcessWrapper;

import java.io.IOException;
import java.util.List;

public class EngineWorkerStockfishImpl implements EngineWorker {
    private Stockfish stockfish;

    @Override
    public void start() throws IOException {
        StockfishConfig stockfishConfig =
                new StockfishConfig().setReadTimeoutInMs(1000);
        this.stockfish = new Stockfish(new ProcessWrapper("stockfish"),
                stockfishConfig);
        this.stockfish.start();
    }

    @Override
    public void stop() throws IOException {
        this.stockfish.stop();
    }

    @Override
    public boolean isValid() {
        return this.stockfish.healthCheck();
    }

    @Override
    public List<String> getPossibleMoves(String fen) {
        try {
            this.stockfish.moveToFenPosition(fen);
            return this.stockfish.getPossibleMoves();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.xchess.engine.api.pool.worker.impl;

import com.xchess.engine.api.pool.worker.EngineWorker;
import com.xchess.main.Stockfish;
import com.xchess.main.StockfishConfig;

import java.io.IOException;

public class EngineWorkerStockfishImpl implements EngineWorker {
    private Stockfish stockfish;

    @Override
    public void start() throws IOException {
        StockfishConfig config = new StockfishConfig().setPath("stockfish");
        this.stockfish = new Stockfish(config);
        this.stockfish.start();
    }

    @Override
    public void runCommand(String command) throws IOException {
//        OutputStream stdin = this.process.getOutputStream();
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
//
//        writer.write(command);
//        writer.flush();
//        writer.close();
    }

    @Override
    public void stop() {
        this.stockfish.stop();
    }
}

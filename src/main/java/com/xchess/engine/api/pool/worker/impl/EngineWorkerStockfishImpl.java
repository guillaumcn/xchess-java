package com.xchess.engine.api.pool.worker.impl;

import com.xchess.engine.api.pool.worker.EngineWorker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class EngineWorkerStockfishImpl implements EngineWorker {
    private Process process;

    @Override
    public void start() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("stockfish");
        this.process = builder.start();
    }

    @Override
    public void runCommand(String command) throws IOException {
        OutputStream stdin = this.process.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        writer.write(command);
        writer.flush();
        writer.close();
    }

    @Override
    public void stop() {
        this.process.destroyForcibly();
    }
}

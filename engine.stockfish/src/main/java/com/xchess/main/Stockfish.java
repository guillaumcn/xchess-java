package com.xchess.main;

import java.io.IOException;

public class Stockfish {
    private StockfishConfig config;
    private Process process;

    public Stockfish(StockfishConfig config) {
        this.config = config;
    }

    public StockfishConfig getConfig() {
        return config;
    }

    public void setConfig(StockfishConfig config) {
        this.config = config;
    }

    public void start() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.config.getPath());
        this.process = builder.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroy()));
    }

    public void stop() {
        this.process.destroyForcibly();
    }
}

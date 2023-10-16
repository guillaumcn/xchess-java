package com.xchess.main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Stockfish {
    private StockfishConfig config;
    private Process process;
    private BufferedWriter writer;
    private BufferedReader stdoutReader;
    private BufferedReader stderrReader;

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
        ProcessBuilder builder = new ProcessBuilder("stockfish");
        this.process = builder.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroy()));

        this.writer = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
        this.stdoutReader =
                new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        this.stderrReader =
                new BufferedReader(new InputStreamReader(this.process.getErrorStream()));

        this.receiveData();
    }

    private void sendCommand(String command) throws IOException {
        this.writer.write(command);
        this.writer.newLine();
        this.writer.flush();
    }

    private List<String> receiveData() throws IOException {
        this.sendCommand("isready");
        List<String> results = new ArrayList<>();
        while (true) {
            String line = stdoutReader.readLine();
            if (!line.isEmpty()) {
                if (line.equals("readyok")) {
                    break;
                } else {
                    results.add(line);
                }
            }
        }
        return results;
    }

    public List<String> test() throws IOException {
        this.sendCommand("go movetime 10000");
//        this.sendCommand("position startpos");
//        this.sendCommand("go movetime 10000");
        return receiveData();
    }


    public void stop() throws IOException {
        this.writer.close();
        this.stdoutReader.close();
        this.process.destroyForcibly();
    }
}

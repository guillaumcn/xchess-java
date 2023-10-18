package com.xchess.main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stockfish {
    private StockfishConfig config;
    private Process process;
    private BufferedWriter writer;
    private BufferedReader stdoutReader;
    private BufferedReader stderrReader;

    public Stockfish(StockfishConfig config) {
        this.config = config;
    }

    public void start() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(config.getPath());
        this.process = builder.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroy()));

        this.writer = new BufferedWriter(new OutputStreamWriter(this.process.getOutputStream()));
        this.stdoutReader =
                new BufferedReader(new InputStreamReader(this.process.getInputStream()));
        this.stderrReader =
                new BufferedReader(new InputStreamReader(this.process.getErrorStream()));

        this.waitUntilReady();
    }

    private List<String> getLineUntil(Pattern responsePattern) throws IOException {
        List<String> results = new ArrayList<>();
        while (true) {
            String line = stdoutReader.readLine();
            if (!line.isEmpty()) {
                results.add(line);
                Matcher matcher = responsePattern.matcher(line);
                if (matcher.matches()) {
                    break;
                }
            }
        }
        return results;
    }

    private void writeCommand(String command) throws IOException {
        this.writer.write(command);
        this.writer.newLine();
        this.writer.flush();
    }

    private void waitUntilReady() throws IOException {
        this.writeCommand("isready");
        getLineUntil(Pattern.compile("^readyok$"));
    }

    public List<String> test() throws IOException, InterruptedException {
        this.waitUntilReady();
        this.writeCommand("go infinite");
        Thread.sleep(1000);
        this.writeCommand("stop");
        return getLineUntil(Pattern.compile("^bestmove.+?"));
    }


    public void stop() throws IOException {
        this.writer.close();
        this.stdoutReader.close();
        this.process.destroyForcibly();
    }
}

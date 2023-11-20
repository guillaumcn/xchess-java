package com.xchess;

import com.xchess.option.StockfishOptions;
import com.xchess.process.ProcessWrapper;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Stockfish {
    private ProcessWrapper process;


    public Stockfish(ProcessWrapper process) {
        this.process = process;
    }

    public void start() throws IOException, InterruptedException {
        this.process.start();
        this.waitUntilReady();
    }

    public void stop() throws IOException {
        this.process.stop();
    }


    public void setOptions(StockfishOptions options) throws IOException, InterruptedException {
        List<String> commands = options.toCommands();
        for (String command :
                commands) {
            this.process.writeCommand(command);
            this.waitUntilReady();
        }
    }

    public void moveToStartPosition() throws IOException, InterruptedException {
        this.process.writeCommand("ucinewgame");
        this.process.writeCommand("position startpos");
        this.waitUntilReady();
    }

    public void moveToFen(String fen) throws IOException, InterruptedException {
        this.process.writeCommand("ucinewgame");
        this.process.writeCommand("position fen " + fen);
        this.waitUntilReady();
    }

    public List<String> getPossibleMoves() throws IOException, InterruptedException {
        this.process.writeCommand("go perft 1");
        List<String> lines = this.process.getLineUntil(Pattern.compile("^Nodes searched.+?$"));
        return lines.stream().filter((line) -> Pattern.matches("^....: 1$", line)).map((line) -> line.split(":")[0]).collect(Collectors.toList());
    }

    public boolean healthCheck() {
        try {
            this.waitUntilReady();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void waitUntilReady() throws IOException, InterruptedException {
        this.process.writeCommand("isready");
        this.process.getLineUntil(Pattern.compile("^readyok$"));
    }
}

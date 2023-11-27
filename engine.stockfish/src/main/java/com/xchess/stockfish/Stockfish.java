package com.xchess.stockfish;

import com.xchess.stockfish.command.EvaluationCommandBuilder;
import com.xchess.stockfish.config.StockfishConfig;
import com.xchess.stockfish.evaluation.StockfishEvaluation;
import com.xchess.stockfish.evaluation.StockfishEvaluationType;
import com.xchess.stockfish.option.StockfishOptions;
import com.xchess.stockfish.process.ProcessWrapper;
import com.xchess.stockfish.validators.FenSyntaxValidator;
import com.xchess.stockfish.validators.MoveValidator;
import com.xchess.stockfish.validators.SquareValidator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class Stockfish {
    private final ProcessWrapper process;
    private final StockfishConfig config;
    private StockfishOptions options;
    private final Float engineVersion;


    public Stockfish(ProcessWrapper process, StockfishConfig config) throws IOException, TimeoutException {
        this.process = process;
        this.config = config;
        this.options = new StockfishOptions();

        this.process.start();
        this.process.writeCommand("uci");
        List<String> initLines = this.waitUntilReady();
        String initLine = initLines.stream().filter(line -> line.startsWith(
                "Stockfish")).findFirst().orElseThrow(IOException::new);
        this.engineVersion = Float.parseFloat(initLine.split(" ")[1]);
    }

    public void stop() throws IOException {
        this.process.stop();
    }

    public Float getEngineVersion() {
        return engineVersion;
    }

    public void setOptions(StockfishOptions options) throws IOException,
            TimeoutException {
        this.options = this.options.merge(options);
        List<String> commands = this.options.getCommands();
        for (String command :
                commands) {
            this.process.writeCommand(command);
            this.waitUntilReady();
        }
    }

    public void setDefaultOptions() throws IOException, TimeoutException {
        this.setOptions(StockfishOptions.getDefaultOptions());
    }

    public String getFenPosition() throws IOException, TimeoutException {
        this.process.writeCommand("d");
        List<String> lines = this.process.readLinesUntil(Pattern.compile(
                        "^Checkers.*$"),
                this.config.getTimeoutInMs());
        this.waitUntilReady();

        String fenLineOptional =
                lines.stream().filter(line -> line.startsWith("Fen")).findFirst().orElseThrow(IOException::new);

        return fenLineOptional.substring(5);
    }

    public List<String> getPossibleMoves() throws IOException,
            TimeoutException {
        this.process.writeCommand("go perft 1");
        List<String> lines = this.process.readLinesUntil(Pattern.compile(
                        "^Nodes searched.*$"),
                this.config.getTimeoutInMs());
        this.waitUntilReady();
        return lines.stream().filter(line -> Pattern.matches("^....: 1$",
                line)).map(line -> line.split(":")[0]).toList();
    }

    public List<String> getPossibleMoves(String square) throws IOException,
            TimeoutException {
        if (SquareValidator.isSquareSyntaxValid(square)) {
            throw new IllegalArgumentException("Invalid syntax for square " + square);
        }
        return this.getPossibleMoves().stream().filter(move -> move.startsWith(square)).toList();
    }

    public boolean isMovePossible(String move) throws IOException,
            TimeoutException {
        if (MoveValidator.isMoveValid(move)) {
            throw new IllegalArgumentException("Invalid syntax for move " + move);
        }
        return getPossibleMoves().contains(move);
    }

    public boolean isValidFenPosition(String fen) {
        if (!FenSyntaxValidator.isFenSyntaxValid(fen)) {
            return false;
        }
        AtomicBoolean isValid = new AtomicBoolean(false);

        Thread t = new Thread(() -> {
            Stockfish tempStockfish = null;
            try {
                tempStockfish =
                        new Stockfish(new ProcessWrapper(this.process.getCommand()),
                                this.config);
                tempStockfish.moveToFenPosition(fen);
                String bestMove =
                        tempStockfish.findBestMove(new EvaluationCommandBuilder().setDepth(10));
                isValid.set(!Objects.isNull(bestMove));
            } catch (IOException | TimeoutException e) {
                isValid.set(false);
            }

            try {
                if (!Objects.isNull(tempStockfish)) {
                    tempStockfish.stop();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return isValid.get();
    }

    public void move(List<String> moves) throws IOException, TimeoutException {
        String startingPosition = this.getFenPosition();
        int invalidMoveIndex =
                moves.stream().map(MoveValidator::isMoveValid).toList().indexOf(false);
        if (invalidMoveIndex != -1) {
            throw new IllegalArgumentException("Invalid syntax for move " + moves.get(invalidMoveIndex));
        }
        for (String move :
                moves) {
            try {
                if (this.isMovePossible(move)) {
                    this.process.writeCommand("position fen " + this.getFenPosition() + " moves " + move);
                    this.waitUntilReady();
                } else {
                    this.moveToFenPosition(startingPosition);
                    throw new IllegalArgumentException("Illegal move " + move +
                            " from position " + this.getFenPosition());
                }
            } catch (TimeoutException e) {
                this.moveToFenPosition(startingPosition);
                throw e;
            }

        }
    }

    public String moveToStartPosition() throws IOException, TimeoutException {
        this.process.writeCommand("position startpos");
        this.waitUntilReady();
        return this.getFenPosition();
    }

    public void moveToFenPosition(String fen) throws IOException,
            TimeoutException {
        this.process.writeCommand("position fen " + fen);
        this.waitUntilReady();
    }

    public String findBestMove(EvaluationCommandBuilder options) throws IOException, TimeoutException {
        this.process.writeCommand(options.build());

        return this.getBestMoveFromOutput();
    }

    public StockfishEvaluation getPositionEvaluation(EvaluationCommandBuilder options) throws IOException,
            TimeoutException {
        String currentFen = this.getFenPosition();
        int multiplier = currentFen.contains("w") ? 1 : -1;

        this.process.writeCommand(options.build());
        List<String> evaluationLines = this.getEvaluationLines();
        Collections.reverse(evaluationLines);

        String lastInfoLine =
                evaluationLines.stream().filter((line) -> line.contains("info"
                ) && line.contains("score")).findFirst().orElseThrow(IOException::new);
        lastInfoLine = lastInfoLine.substring(lastInfoLine.indexOf("score"));
        String[] splittedLastInfoLine = lastInfoLine.split(" ");
        String type = splittedLastInfoLine[1];
        String value = splittedLastInfoLine[2];
        return new StockfishEvaluation(type.equals("cp") ?
                StockfishEvaluationType.CENTIPAWNS :
                StockfishEvaluationType.MATE,
                Integer.parseInt(value) * multiplier);
    }

    private String getBestMoveFromOutput() throws IOException,
            TimeoutException {
        List<String> evaluationLines = getEvaluationLines();
        String bestmoveLine =
                evaluationLines.stream().filter(line -> line.startsWith(
                        "bestmove")).findFirst().orElseThrow(IOException::new);

        String bestMove = bestmoveLine.split(" ")[1];
        return bestMove.equals("(none)") ? null : bestMove;
    }

    private List<String> getEvaluationLines() throws TimeoutException,
            IOException {
        List<String> bestMoveLines =
                this.process.readLinesUntil(Pattern.compile("^bestmove.*$"),
                        this.config.getTimeoutInMs());
        this.waitUntilReady();
        return bestMoveLines;
    }

    public boolean healthCheck() {
        try {
            this.waitUntilReady();
        } catch (IOException | TimeoutException e) {
            return false;
        }
        return true;
    }

    private List<String> waitUntilReady() throws IOException, TimeoutException {
        this.process.writeCommand("isready");
        return this.process.readLinesUntil("readyok",
                this.config.getTimeoutInMs());
    }
}

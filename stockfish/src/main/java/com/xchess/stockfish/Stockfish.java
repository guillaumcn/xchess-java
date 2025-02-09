package com.xchess.stockfish;

import com.xchess.ChessEngine;
import com.xchess.evaluation.ChessEngineEvaluation;
import com.xchess.evaluation.ChessEngineEvaluationType;
import com.xchess.evaluation.parameter.EvaluationParameters;
import com.xchess.exceptions.*;
import com.xchess.process.ProcessWrapper;
import com.xchess.stockfish.config.StockfishConfig;
import com.xchess.stockfish.option.StockfishOptions;
import com.xchess.validators.MoveValidator;
import com.xchess.validators.SquareValidator;
import lombok.Getter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

/**
 * Java implementation for Stockfish engine
 */
public class Stockfish implements ChessEngine {
    private final ProcessWrapper process;
    private final StockfishConfig config;
    @Getter
    private StockfishOptions options;
    @Getter
    private final Float engineVersion;

    /**
     * Creates an instance of Stockfish implementation. When creating an
     * instance the default Stockfish engine options will be set.
     * See {@link StockfishOptions#getDefaultOptions()}
     *
     * @param process The process wrapper instance created with stockfish
     *                startup command
     * @param config  The Stockfish configuration object
     * @throws IOException      If any error occurs communicating with
     *                          Stockfish engine process
     * @throws TimeoutException in case of timeout reached when reading
     */
    public Stockfish(ProcessWrapper process, StockfishConfig config) throws IOException,
            TimeoutException {
        this.process = process;
        this.config = config;
        this.options = StockfishOptions.getDefaultOptions();

        this.process.start();
        this.process.writeCommand("uci");
        List<String> initLines = waitUntilReady();
        String initLine = initLines.stream().filter(line -> line.startsWith(
                "Stockfish")).findFirst().orElseThrow(() -> new IOException(
                "Cannot find stockfish initialization line"));
        this.engineVersion = Float.parseFloat(initLine.split(" ")[1]);

        setOptions(options);
    }

    public void stop() throws IOException {
        process.stop();
    }

    /**
     * Set current Stockfish engine options. New options will be merged with
     * current. See {@link StockfishOptions#merge(StockfishOptions)}
     *
     * @param options The options to set
     * @throws IOException      If any error occurs communicating with
     *                          Stockfish engine
     *                          process
     * @throws TimeoutException in case of timeout reached when reading
     */
    public synchronized void setOptions(StockfishOptions options) throws IOException,
            TimeoutException {
        this.options = this.options.merge(options);
        List<String> commands = this.options.getCommands();
        for (String command :
                commands) {
            process.writeCommand(command);
            waitUntilReady();
        }
    }

    public synchronized String getFenPosition() throws IOException,
            TimeoutException {
        process.writeCommand("d");
        List<String> lines = process.readLinesUntil(Pattern.compile(
                        "^Checkers.*$"),
                config.getTimeoutInMs());
        waitUntilReady();

        String fenLineOptional =
                lines.stream().filter(line -> line.startsWith("Fen")).findFirst().orElseThrow(() -> new IOException("Cannot find fen line in output"));

        return fenLineOptional.substring(5);
    }

    public synchronized List<String> getPossibleMoves() throws IOException,
            TimeoutException {
        process.writeCommand("go perft 1");
        List<String> lines = process.readLinesUntil(Pattern.compile(
                        "^Nodes searched.*$"),
                config.getTimeoutInMs());
        waitUntilReady();
        return lines.stream().filter(line -> Pattern.matches("^....: 1$",
                line)).map(line -> line.split(":")[0]).toList();
    }

    public synchronized List<String> getPossibleMoves(String square) throws IOException,
            TimeoutException, InvalidSquareSyntaxException {
        String lowerCaseSquare = square.toLowerCase();
        if (!SquareValidator.isSquareSyntaxValid(lowerCaseSquare)) {
            throw new InvalidSquareSyntaxException(square);
        }
        return getPossibleMoves().stream().filter(move -> move.startsWith(lowerCaseSquare)).toList();
    }

    public synchronized boolean isMovePossible(String move) throws IOException,
            TimeoutException, InvalidMoveSyntaxException {
        String lowerCaseMove = move.toLowerCase();
        if (!MoveValidator.isMoveValid(lowerCaseMove)) {
            throw new InvalidMoveSyntaxException(move);
        }
        return getPossibleMoves().contains(lowerCaseMove);
    }

    public synchronized void moveToStartPosition(boolean newGame) throws IOException,
            TimeoutException {
        if (newGame) {
            process.writeCommand("ucinewgame");
        }
        process.writeCommand("position startpos");
        waitUntilReady();
    }

    public synchronized void moveToFenPosition(String fen, boolean newGame) throws IOException,
            TimeoutException, InvalidFenPositionException {
        if (newGame) {
            process.writeCommand("ucinewgame");
        }
        process.writeCommand("position fen " + fen);
        try {
            waitUntilReady();
        } catch (ProcessKilledException e) {
            throw new InvalidFenPositionException(fen);
        }
    }

    public synchronized void move(List<String> moves) throws IOException,
            TimeoutException, InvalidMoveSyntaxException,
            IllegalMoveException, InvalidFenPositionException {
        List<String> lowerCasesMoves =
                moves.stream().map(String::toLowerCase).toList();
        int invalidMoveIndex =
                lowerCasesMoves.stream().map(MoveValidator::isMoveValid).toList().indexOf(false);
        if (invalidMoveIndex != -1) {
            throw new InvalidMoveSyntaxException(moves.get(invalidMoveIndex));
        }
        String startingPosition = getFenPosition();
        for (String move :
                lowerCasesMoves) {
            try {
                if (isMovePossible(move)) {
                    process.writeCommand("position fen " + getFenPosition() + " moves " + move);
                    waitUntilReady();
                } else {
                    moveToFenPosition(startingPosition, false);
                    throw new IllegalMoveException(move, getFenPosition());
                }
            } catch (TimeoutException e) {
                moveToFenPosition(startingPosition, false);
                throw e;
            }

        }
    }

    public synchronized String findBestMove(EvaluationParameters options) throws IOException, TimeoutException {
        process.writeCommand(options.buildCommand());

        return getBestMoveFromOutput();
    }

    public synchronized ChessEngineEvaluation getPositionEvaluation(EvaluationParameters options) throws IOException,
            TimeoutException {
        String currentFen = getFenPosition();
        int multiplier = currentFen.contains("w") ? 1 : -1;

        process.writeCommand(options.buildCommand());
        List<String> evaluationLines = getEvaluationLines();
        Collections.reverse(evaluationLines);

        String lastInfoLine =
                evaluationLines.stream().filter(line -> line.contains("info"
                ) && line.contains("score")).findFirst().orElseThrow(IOException::new);
        lastInfoLine = lastInfoLine.substring(lastInfoLine.indexOf("score"));
        String[] splittedLastInfoLine = lastInfoLine.split(" ");
        String type = splittedLastInfoLine[1];
        String value = splittedLastInfoLine[2];
        return ChessEngineEvaluation.builder()
                .type("cp".equals(type) ?
                        ChessEngineEvaluationType.CENTIPAWNS :
                        ChessEngineEvaluationType.MATE)
                .value(Integer.parseInt(value) * multiplier)
                .build();
    }

    public synchronized boolean healthCheck() {
        try {
            waitUntilReady();
        } catch (IOException | TimeoutException e) {
            return false;
        }
        return true;
    }

    /**
     * @return The best move from process output
     * @throws IOException      If any error occurs communicating with
     *                          Stockfish engine process
     * @throws TimeoutException if read timeout
     */
    private String getBestMoveFromOutput() throws IOException,
            TimeoutException {
        List<String> evaluationLines = getEvaluationLines();
        String bestmoveLine =
                evaluationLines.stream().filter(line -> line.startsWith(
                        "bestmove")).findFirst().orElseThrow(IOException::new);

        String bestMove = bestmoveLine.split(" ")[1];
        return "(none)".equals(bestMove) ? null : bestMove;
    }

    /**
     * @return Evaluation lines from evaluation request
     * @throws TimeoutException if read timeout
     * @throws IOException      If any error occurs communicating with
     *                          Stockfish engine process
     */
    private List<String> getEvaluationLines() throws TimeoutException,
            IOException {
        List<String> bestMoveLines =
                process.readLinesUntil(Pattern.compile("^bestmove.*$"),
                        config.getTimeoutInMs());
        waitUntilReady();
        return bestMoveLines;
    }

    /**
     * @return list of messages received before "readyok"
     * @throws IOException      If any error occurs communicating with
     *                          Stockfish engine process
     * @throws TimeoutException if read timeout
     */
    protected List<String> waitUntilReady() throws IOException,
            TimeoutException {
        process.writeCommand("isready");
        return process.readLinesUntil("readyok",
                config.getTimeoutInMs());
    }
}

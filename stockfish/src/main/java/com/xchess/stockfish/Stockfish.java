package com.xchess.stockfish;

import com.xchess.ChessEngine;
import com.xchess.evaluation.ChessEngineEvaluation;
import com.xchess.evaluation.ChessEngineEvaluationType;
import com.xchess.evaluation.parameter.EvaluationParameters;
import com.xchess.process.ProcessWrapper;
import com.xchess.stockfish.config.StockfishConfig;
import com.xchess.stockfish.option.StockfishOptions;
import com.xchess.validators.MoveValidator;
import com.xchess.validators.SquareValidator;

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
    private StockfishOptions options;
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
        List<String> initLines = this.waitUntilReady();
        String initLine = initLines.stream().filter(line -> line.startsWith(
                "Stockfish")).findFirst().orElseThrow(() -> new IOException(
                "Cannot find stockfish initialization line"));
        this.engineVersion = Float.parseFloat(initLine.split(" ")[1]);

        this.setOptions(options);
    }

    public void stop() throws IOException {
        this.process.stop();
    }

    /**
     * Get current Stockfish engine options
     *
     * @return the current options
     */
    public StockfishOptions getOptions() {
        return this.options;
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
            this.process.writeCommand(command);
            this.waitUntilReady();
        }
    }

    public Float getEngineVersion() {
        return engineVersion;
    }

    public synchronized String getFenPosition() throws IOException,
            TimeoutException {
        this.process.writeCommand("d");
        List<String> lines = this.process.readLinesUntil(Pattern.compile(
                        "^Checkers.*$"),
                this.config.getTimeoutInMs());
        this.waitUntilReady();

        String fenLineOptional =
                lines.stream().filter(line -> line.startsWith("Fen")).findFirst().orElseThrow(() -> new IOException("Cannot find fen line in output"));

        return fenLineOptional.substring(5);
    }

    public synchronized List<String> getPossibleMoves() throws IOException,
            TimeoutException {
        this.process.writeCommand("go perft 1");
        List<String> lines = this.process.readLinesUntil(Pattern.compile(
                        "^Nodes searched.*$"),
                this.config.getTimeoutInMs());
        this.waitUntilReady();
        return lines.stream().filter(line -> Pattern.matches("^....: 1$",
                line)).map(line -> line.split(":")[0]).toList();
    }

    public synchronized List<String> getPossibleMoves(String square) throws IOException,
            TimeoutException {
        String lowerCaseSquare = square.toLowerCase();
        if (!SquareValidator.isSquareSyntaxValid(lowerCaseSquare)) {
            throw new IllegalArgumentException("Invalid syntax for square " + square);
        }
        return this.getPossibleMoves().stream().filter(move -> move.startsWith(lowerCaseSquare)).toList();
    }

    public synchronized boolean isMovePossible(String move) throws IOException,
            TimeoutException {
        String lowerCaseMove = move.toLowerCase();
        if (!MoveValidator.isMoveValid(lowerCaseMove)) {
            throw new IllegalArgumentException("Invalid syntax for move " + move);
        }
        return getPossibleMoves().contains(lowerCaseMove);
    }

    public synchronized void moveToStartPosition(boolean newGame) throws IOException,
            TimeoutException {
        if (newGame) {
            this.process.writeCommand("ucinewgame");
        }
        this.process.writeCommand("position startpos");
        this.waitUntilReady();
    }

    public synchronized void moveToFenPosition(String fen, boolean newGame) throws IOException,
            TimeoutException {
        if (newGame) {
            this.process.writeCommand("ucinewgame");
        }
        this.process.writeCommand("position fen " + fen);
        this.waitUntilReady();
    }

    public synchronized void move(List<String> moves) throws IOException,
            TimeoutException {
        List<String> lowerCasesMoves =
                moves.stream().map(String::toLowerCase).toList();
        int invalidMoveIndex =
                lowerCasesMoves.stream().map(MoveValidator::isMoveValid).toList().indexOf(false);
        if (invalidMoveIndex != -1) {
            throw new IllegalArgumentException("Invalid syntax for move " + moves.get(invalidMoveIndex));
        }
        String startingPosition = this.getFenPosition();
        for (String move :
                lowerCasesMoves) {
            try {
                if (this.isMovePossible(move)) {
                    this.process.writeCommand("position fen " + this.getFenPosition() + " moves " + move);
                    this.waitUntilReady();
                } else {
                    this.moveToFenPosition(startingPosition, false);
                    throw new IllegalArgumentException("Illegal move " + move +
                            " from position " + this.getFenPosition());
                }
            } catch (TimeoutException e) {
                this.moveToFenPosition(startingPosition, false);
                throw e;
            }

        }
    }

    public synchronized String findBestMove(EvaluationParameters options) throws IOException, TimeoutException {
        this.process.writeCommand(options.getCommand());

        return this.getBestMoveFromOutput();
    }

    public synchronized ChessEngineEvaluation getPositionEvaluation(EvaluationParameters options) throws IOException,
            TimeoutException {
        String currentFen = this.getFenPosition();
        int multiplier = currentFen.contains("w") ? 1 : -1;

        this.process.writeCommand(options.getCommand());
        List<String> evaluationLines = this.getEvaluationLines();
        Collections.reverse(evaluationLines);

        String lastInfoLine =
                evaluationLines.stream().filter(line -> line.contains("info"
                ) && line.contains("score")).findFirst().orElseThrow(IOException::new);
        lastInfoLine = lastInfoLine.substring(lastInfoLine.indexOf("score"));
        String[] splittedLastInfoLine = lastInfoLine.split(" ");
        String type = splittedLastInfoLine[1];
        String value = splittedLastInfoLine[2];
        return new ChessEngineEvaluation(type.equals("cp") ?
                ChessEngineEvaluationType.CENTIPAWNS :
                ChessEngineEvaluationType.MATE,
                Integer.parseInt(value) * multiplier);
    }

    public synchronized boolean healthCheck() {
        try {
            this.waitUntilReady();
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
        return bestMove.equals("(none)") ? null : bestMove;
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
                this.process.readLinesUntil(Pattern.compile("^bestmove.*$"),
                        this.config.getTimeoutInMs());
        this.waitUntilReady();
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
        this.process.writeCommand("isready");
        return this.process.readLinesUntil("readyok",
                this.config.getTimeoutInMs());
    }
}

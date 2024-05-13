package com.xchess;

import com.xchess.evaluation.ChessEngineEvaluation;
import com.xchess.evaluation.parameter.EvaluationParameters;
import com.xchess.exceptions.IllegalMoveException;
import com.xchess.exceptions.InvalidFenPositionException;
import com.xchess.exceptions.InvalidMoveSyntaxException;
import com.xchess.exceptions.InvalidSquareSyntaxException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface ChessEngine {
    /**
     * Stop the engine
     *
     * @throws IOException If any error occurs communicating with Stockfish
     *                     engine process
     */
    void stop() throws IOException;

    /**
     * @return The engine version
     */
    Float getEngineVersion();

    /**
     * @return Current fen position
     * @throws IOException      If any error occurs communicating with
     *                          engine
     *                          process
     * @throws TimeoutException In case of timeout reached when reading
     */
    String getFenPosition() throws IOException, TimeoutException;

    /**
     * @return Possible moves from current position. See
     * {@link #moveToFenPosition(String, boolean)} or
     * {@link #moveToStartPosition(boolean)} to change current position
     * @throws IOException      If any error occurs communicating with
     *                          engine process
     * @throws TimeoutException In case of timeout reached when reading
     */
    List<String> getPossibleMoves() throws IOException, TimeoutException;

    /**
     * @param square The square to check with format "a1"
     * @return Possible moves from current position. See
     * {@link #moveToFenPosition(String, boolean)} or
     * {@link #moveToStartPosition(boolean)} to change current position
     * @throws IOException      If any error occurs communicating with
     *                          engine process
     * @throws TimeoutException In case of timeout reached when reading
     */
    List<String> getPossibleMoves(String square) throws IOException,
            TimeoutException, InvalidSquareSyntaxException;

    /**
     * Check if a move is possible from current position. See
     * {@link #moveToFenPosition(String, boolean)} or
     * {@link #moveToStartPosition(boolean)} to change current position
     *
     * @param move The move to check with format "a1a2" or "a1a2r"
     * @return true if the move is possible
     * @throws IOException      If any error occurs communicating with
     *                          engine process
     * @throws TimeoutException In case of timeout reached when reading
     */
    boolean isMovePossible(String move) throws IOException,
            TimeoutException, InvalidMoveSyntaxException;

    /**
     * Move from current position. See
     * {@link #moveToFenPosition(String, boolean)} or
     * {@link #moveToStartPosition(boolean)} to change current position
     *
     * @param moves The moves to with format "a1a2" or "a1a2r"
     * @throws IOException                 If any error occurs communicating
     *                                     with
     *                                     engine process
     * @throws TimeoutException            In case of timeout reached when
     *                                     reading
     * @throws InvalidMoveSyntaxException  Invalid move syntax
     * @throws IllegalMoveException        Illegal move
     * @throws InvalidFenPositionException Invalid fen position when moving
     */
    void move(List<String> moves) throws IOException, TimeoutException,
            InvalidMoveSyntaxException, IllegalMoveException,
            InvalidFenPositionException;

    /**
     * Move to the start position
     *
     * @param newGame true to tell engine to start a new and clear hash table
     * @throws IOException      If any error occurs communicating with engine
     *                          process
     * @throws TimeoutException In case of timeout reached when reading
     */
    void moveToStartPosition(boolean newGame) throws IOException,
            TimeoutException;

    /**
     * Move to fen position
     *
     * @param fen     the fen position to move to
     * @param newGame true to tell engine to start a new and clear hash table
     * @throws IOException                 If any error occurs communicating
     * with engine
     *                                     process
     * @throws TimeoutException            In case of timeout reached when
     * reading
     * @throws InvalidFenPositionException If given fen position is invalid
     */
    void moveToFenPosition(String fen, boolean newGame) throws IOException,
            TimeoutException, InvalidFenPositionException;

    /**
     * Find the best move in current position
     *
     * @param options the evaluation parameters. See
     *                {@link EvaluationParameters}
     * @return The best move in format "a1a2"
     * @throws IOException      If any error occurs communicating with engine
     *                          process
     * @throws TimeoutException In case of timeout reached when reading
     */
    String findBestMove(EvaluationParameters options) throws IOException,
            TimeoutException;

    /**
     * @param options the evaluation parameters. See
     *                {@link EvaluationParameters}
     * @return The chess engine evaluation. See {@link ChessEngineEvaluation}
     * @throws IOException      If any error occurs communicating with engine
     *                          process
     * @throws TimeoutException In case of timeout reached when reading
     */
    ChessEngineEvaluation getPositionEvaluation(EvaluationParameters options) throws IOException, TimeoutException;

    /**
     * @return The current engine status
     */
    boolean healthCheck();
}

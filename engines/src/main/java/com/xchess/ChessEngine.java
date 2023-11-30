package com.xchess;

import com.xchess.evaluation.ChessEngineEvaluation;
import com.xchess.evaluation.parameters.EvaluationParameters;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface ChessEngine {
    void stop() throws IOException;

    Float getEngineVersion();

    String getFenPosition() throws IOException, TimeoutException;

    List<String> getPossibleMoves() throws IOException, TimeoutException;

    List<String> getPossibleMoves(String square) throws IOException,
            TimeoutException;

    boolean isMovePossible(String move) throws IOException,
            TimeoutException;

    boolean isValidFenPosition(String fen) throws IOException, TimeoutException;

    void move(List<String> moves) throws IOException, TimeoutException;

    String moveToStartPosition() throws IOException, TimeoutException;

    void moveToFenPosition(String fen) throws IOException,
            TimeoutException;

    String findBestMove(EvaluationParameters options) throws IOException,
            TimeoutException;

    ChessEngineEvaluation getPositionEvaluation(EvaluationParameters options) throws IOException, TimeoutException;

    boolean healthCheck();
}

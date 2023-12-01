package com.xchess.evaluation;

/**
 * Represents the current evaluation of a game, with negative score for black
 * advantage and positive score for white advantage
 *
 * @param type  The type of evaluation could be mate or centipawns
 * @param value The value of evaluation could be positive for white advantage
 *             and negative for black advantage
 */
public record ChessEngineEvaluation(ChessEngineEvaluationType type, int value) {
}

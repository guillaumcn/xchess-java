package com.xchess.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChessEngineEvaluation {
    private ChessEngineEvaluationType type;
    private int value;
}

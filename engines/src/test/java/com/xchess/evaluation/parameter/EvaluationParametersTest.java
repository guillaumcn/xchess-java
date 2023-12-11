package com.xchess.evaluation.parameter;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

public class EvaluationParametersTest {
    @Test
    public void shouldBuildEvaluationCommandString() {
        EvaluationParameters ep = EvaluationParameters.builder()
                .binc(2)
                .btime(3)
                .depth(4)
                .mate(2)
                .movestogo(2)
                .movetime(3)
                .nodes(4)
                .searchMoves(Arrays.asList("a2a4", "e2e4"))
                .winc(2)
                .wtime(3)
                .build();
        assertEquals("go searchmoves a2a4 e2e4 wtime 3 btime 3 winc 2 binc 2 " +
                        "movestogo 2 depth 4 nodes 4 mate 2 movetime 3",
                ep.buildCommand());
    }

    @Test
    public void shouldBuildEvaluationCommandStringOnlyWithSetParams() {
        EvaluationParameters ep = EvaluationParameters.builder()
                .binc(2)
                .build();
        assertEquals("go binc 2", ep.buildCommand());
    }
}

package com.xchess.evaluation.parameter;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

public class EvaluationParametersTest {
    @Test
    public void shouldBuildEvaluationCommandString() {
        EvaluationParameters ep = new EvaluationParameters()
                .setBinc(2)
                .setBtime(3)
                .setDepth(4)
                .setMate(2)
                .setMovestogo(2)
                .setMovetime(3)
                .setNodes(4)
                .setSearchMoves(Arrays.asList("a2a4", "e2e4"))
                .setWinc(2)
                .setWtime(3);
        assertEquals("go searchmoves a2a4 e2e4 wtime 3 btime 3 winc 2 binc 2 " +
                "movestogo 2 depth 4 nodes 4 mate 2 movetime 3", ep.build());
    }

    @Test
    public void shouldBuildEvaluationCommandStringOnlyWithSetParams() {
        EvaluationParameters ep = new EvaluationParameters()
                .setBinc(2);
        assertEquals("go binc 2", ep.build());
    }
}

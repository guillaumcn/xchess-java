package com.xchess.engine.api.controller;

import com.xchess.ChessEngine;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ChessControllerTest {
    private ChessController testSubject;
    private ChessEngine engine;

    @BeforeEach
    public void setUp() throws Exception {
        GenericObjectPool<ChessEngine> pool = mock(GenericObjectPool.class);
        this.engine = mock(ChessEngine.class);
        when(pool.borrowObject()).thenReturn(engine);
        testSubject =
                new ChessController(new PoolWrapperTestImplementation(pool));
    }

    @Test
    public void shouldCallEngineGetVersionMethod() throws Exception {
        doReturn(12.4f).when(this.engine).getEngineVersion();

        assertEquals(12.4f, this.testSubject.getEngineVersion());
    }

    @Test
    public void shouldMoveToFenPositionWhenGettingPossibleMoves() throws Exception {
        String fen = "rnbqkbnr/ppp1pppp/8/3p4/4P3/8" +
                "/PPPP1PPP/RNBQKBNR w KQkq d6 0 2";
        this.testSubject.getPossibleMoves(fen, null);

        verify(this.engine, times(1)).moveToFenPosition(fen, true);
    }

    @Test
    public void shouldMoveToStartPositionWhenGettingPossibleMoves() throws Exception {
        this.testSubject.getPossibleMoves(null, null);

        verify(this.engine, times(1)).moveToStartPosition(true);
    }

    @Test
    public void shouldGetPossibleMoves() throws Exception {
        List<String> expected = Collections.singletonList("a2a4");
        doReturn(expected).when(this.engine).getPossibleMoves();
        List<String> result = this.testSubject.getPossibleMoves(null, null);
        assertEquals(expected, result);
    }

    @Test
    public void shouldGetPossibleMovesForSquare() throws Exception {
        List<String> expected = Collections.singletonList("a2a4");
        doReturn(expected).when(this.engine).getPossibleMoves(eq("a2"));
        List<String> result = this.testSubject.getPossibleMoves(null, "a2");
        assertEquals(expected, result);
    }
}

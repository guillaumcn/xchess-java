package com.xchess.stockfish;

import com.xchess.stockfish.config.StockfishConfig;
import com.xchess.stockfish.option.StockfishOptions;
import com.xchess.process.ProcessWrapper;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StockfishTest {
    private ProcessWrapper process;
    private StockfishConfig config;
    private Stockfish subject;

    @Before
    public void setUp() {
        this.process = mock(ProcessWrapper.class);
        this.config = new StockfishConfig();
    }

    @Test
    public void shouldParseEngineVersionFromInitOutput() throws IOException,
            TimeoutException {
        initStockfishInstance(true);
        assertEquals(14.1, this.subject.getEngineVersion(), 0.1f);
    }

    @Test
    public void shouldThrowIoExceptionIfInitOutputIsInvalid() {
        assertThrows(IOException.class, () -> initStockfishInstance(false));
    }

    @Test
    public void shouldStopProcessOnStop() throws IOException, TimeoutException {
        initStockfishInstance(true);
        this.subject.stop();
        verify(this.process, times(1)).stop();
    }

    @Test
    public void shouldWriteOptionsCommandToProcessInput() throws IOException, TimeoutException {
        initStockfishInstance(true);
        StockfishOptions options = new StockfishOptions()
                .setHash(12)
                .setThreads(25)
                .setDebugLogFile("debugFile.txt");
        this.subject.setOptions(options);
        for (String command:
                options.getCommands()) {
            verify(this.process, times(1)).writeCommand(command);
        }
    }

    @Test
    public void shouldMergeWithCurrentOptions() throws IOException, TimeoutException {
        initStockfishInstance(true);
        StockfishOptions currentOptions = new StockfishOptions()
                .setHash(12)
                .setThreads(25);
        this.subject.setOptions(currentOptions);
        StockfishOptions newOptions = new StockfishOptions()
                .setPonder(false)
                .setThreads(45);
        this.subject.setOptions(newOptions);
        verify(this.process, times(2)).writeCommand("setoption name Hash value 12");
        verify(this.process, times(1)).writeCommand("setoption name Threads value 25");
        verify(this.process, times(1)).writeCommand("setoption name Ponder value false");
        verify(this.process, times(1)).writeCommand("setoption name Threads value 45");
    }

    @Test
    public void shouldGetFenPosition() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/fenPosition.txt",
                "d");
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 " +
                "1", this.subject.getFenPosition());
    }

    @Test
    public void shouldThrowExceptionIfFenPositionOutputIsInvalid() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/fenPositionInvalid" +
                ".txt", "d");
        assertThrows(IOException.class, () -> this.subject.getFenPosition());
    }

    @Test
    public void shouldGetPossibleMoves() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        List<String> result = this.subject.getPossibleMoves();
        assertTrue(result.contains("a2a4"));
        assertTrue(result.contains("g1h3"));
        assertEquals(20, result.size());
    }

    @Test
    public void shouldGetPossibleMovesForSpecificSquare() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        List<String> result = this.subject.getPossibleMoves("a2");
        assertTrue(result.contains("a2a3"));
        assertTrue(result.contains("a2a4"));
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnNoPossibleMovesForSpecificSquare() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        List<String> result = this.subject.getPossibleMoves("a3");
        assertEquals(0, result.size());
    }

    @Test
    public void shouldThrowExceptionIfASquareHasInvalidSyntax() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        assertThrows(IllegalArgumentException.class, () -> this.subject.getPossibleMoves("a9"));
    }

    @Test
    public void shouldReturnTrueIfAMoveIsPossible() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        assertTrue(this.subject.isMovePossible("a2a4"));
    }

    @Test
    public void shouldReturnFalseIfAMoveIsNotPossible() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        assertFalse(this.subject.isMovePossible("a3a4"));
    }

    @Test
    public void shouldThrowExceptionIfAMoveHasInvalidSyntax() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        assertThrows(IllegalArgumentException.class, () -> this.subject.isMovePossible("a9a8"));
    }

    private void initStockfishInstance(boolean validInitOutput) throws IOException, TimeoutException {
        String file = validInitOutput ? "stockfish/outputs/init.txt" :
                "stockfish/outputs/invalidInit.txt";
        this.bindFileToLineReaderWhenWriting(file, "isready");
        this.subject = new Stockfish(process, config);
        verify(this.process, times(1)).writeCommand("uci");
    }

    private void bindFileToLineReaderWhenWriting(String file,
                                                 String whenWriting) throws IOException {
        doAnswer((invocation) -> {
            this.bindFileToLineReader(file);
            return null;
        }).when(this.process).writeCommand(whenWriting);
    }

    private void bindFileToLineReader(String file) throws IOException,
            TimeoutException {
        List<String> fileLines = getResourcesFileLines(file);
        when(this.process.readLinesUntil(any(Pattern.class),
                anyInt())).thenReturn(fileLines);
        when(this.process.readLinesUntil(any(String.class),
                anyInt())).thenReturn(fileLines);
    }

    private List<String> getResourcesFileLines(String file) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        if (Objects.isNull(is)) {
            throw new IllegalArgumentException("Resource not exists " + file);
        }
        List<String> fileLines = new ArrayList<>();
        InputStreamReader streamReader =
                new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        while ((line = reader.readLine()) != null) {
            fileLines.add(line);
        }
        return fileLines;
    }
}

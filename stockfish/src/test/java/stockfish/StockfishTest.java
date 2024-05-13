package stockfish;

import com.xchess.evaluation.ChessEngineEvaluation;
import com.xchess.evaluation.ChessEngineEvaluationType;
import com.xchess.evaluation.parameter.EvaluationParameters;
import com.xchess.exceptions.IllegalMoveException;
import com.xchess.exceptions.InvalidFenPositionException;
import com.xchess.exceptions.InvalidMoveSyntaxException;
import com.xchess.exceptions.InvalidSquareSyntaxException;
import com.xchess.process.ProcessWrapper;
import com.xchess.stockfish.config.StockfishConfig;
import com.xchess.stockfish.option.StockfishOptions;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StockfishTest {
    private ProcessWrapper process;
    private StockfishConfig config;
    private StockfishTestImpl subject;

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
    public void shouldWriteOptionsCommandToProcessInput() throws IOException,
            TimeoutException {
        initStockfishInstance(true);
        StockfishOptions options = StockfishOptions.builder()
                .hash(12)
                .threads(25)
                .debugLogFile("debugFile.txt")
                .build();
        this.subject.setOptions(options);
        for (String command :
                options.getCommands()) {
            verify(this.process, times(1)).writeCommand(command);
        }
    }

    @Test
    public void shouldMergeWithCurrentOptions() throws IOException,
            TimeoutException {
        initStockfishInstance(true);
        StockfishOptions currentOptions = StockfishOptions.getDefaultOptions();
        StockfishOptions newOptions = StockfishOptions.builder()
                .ponder(true)
                .threads(45)
                .build();
        this.subject.setOptions(newOptions);
        assertEquals(currentOptions.merge(newOptions),
                this.subject.getOptions());
        verify(this.process, times(2)).writeCommand("setoption name Hash " +
                "value 16");
        verify(this.process, times(1)).writeCommand("setoption name Ponder " +
                "value true");
        verify(this.process, times(1)).writeCommand("setoption name Threads " +
                "value 45");
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
    public void shouldGetPossibleMovesForSpecificSquare() throws IOException,
            TimeoutException, InvalidSquareSyntaxException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        List<String> result = this.subject.getPossibleMoves("a2");
        assertTrue(result.contains("a2a3"));
        assertTrue(result.contains("a2a4"));
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnNoPossibleMovesForSpecificSquare() throws IOException, TimeoutException, InvalidSquareSyntaxException {
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
        assertThrows(InvalidSquareSyntaxException.class,
                () -> this.subject.getPossibleMoves("a9"));
    }

    @Test
    public void shouldReturnTrueIfAMoveIsPossible() throws IOException,
            TimeoutException, InvalidMoveSyntaxException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/possibleMoves.txt",
                "go perft 1");
        assertTrue(this.subject.isMovePossible("a2a4"));
    }

    @Test
    public void shouldReturnFalseIfAMoveIsNotPossible() throws IOException,
            TimeoutException, InvalidMoveSyntaxException {
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
        assertThrows(InvalidMoveSyntaxException.class,
                () -> this.subject.isMovePossible("a9a8"));
    }

    @Test
    public void shouldMoveToStartPosition() throws IOException,
            TimeoutException {
        initStockfishInstance(true);
        this.subject.moveToStartPosition(false);
        verify(this.process, times(1)).writeCommand("position startpos");
    }

    @Test
    public void shouldMoveToStartPositionWithNewGameCommand() throws IOException, TimeoutException {
        initStockfishInstance(true);
        this.subject.moveToStartPosition(true);
        verify(this.process, times(1)).writeCommand("ucinewgame");
        verify(this.process, times(1)).writeCommand("position startpos");
    }

    @Test
    public void shouldMoveToFenPosition() throws IOException,
            TimeoutException, InvalidFenPositionException {
        initStockfishInstance(true);
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        this.subject.moveToFenPosition(fen, false);
        verify(this.process, times(1)).writeCommand("position fen " + fen);
    }

    @Test
    public void shouldMoveToFenPositionWithNewGameCommand() throws IOException, TimeoutException, InvalidFenPositionException {
        initStockfishInstance(true);
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        this.subject.moveToFenPosition(fen, true);
        verify(this.process, times(1)).writeCommand("ucinewgame");
        verify(this.process, times(1)).writeCommand("position fen " + fen);
    }

    @Test
    public void shouldMove() throws IOException, TimeoutException,
            IllegalMoveException, InvalidMoveSyntaxException,
            InvalidFenPositionException {
        initStockfishInstance(true);
        this.subject.setSuccessiveFens(Arrays.asList(
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
                "rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1",
                "rnbqkbnr/1ppppppp/8/p7/P7/8/1PPPPPPP/RNBQKBNR w KQkq - 0 2"
        ));
        this.subject.setSuccessivePossibleMoves(Arrays.asList(
                Collections.singletonList("a2a4"),
                Collections.singletonList("a7a5"),
                Collections.singletonList("b2b4")
        ));
        this.subject.move(Arrays.asList("a2a4", "a7a5", "b2b4"));

        verify(this.process, times(1)).writeCommand("position fen " +
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 " +
                "moves a2a4");
        verify(this.process, times(1)).writeCommand("position fen " +
                "rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1 " +
                "moves a7a5");
        verify(this.process, times(1)).writeCommand("position fen " +
                "rnbqkbnr/1ppppppp/8/p7/P7/8/1PPPPPPP/RNBQKBNR w KQkq - 0 2 " +
                "moves b2b4");
    }

    @Test
    public void shouldGoBackToInitialPositionIfOneMoveIsNotValidDuringProcess() throws IOException, TimeoutException {
        initStockfishInstance(true);
        this.subject.setSuccessiveFens(Arrays.asList(
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
                "rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1",
                "rnbqkbnr/1ppppppp/8/p7/P7/8/1PPPPPPP/RNBQKBNR w KQkq - 0 2"
        ));
        this.subject.setSuccessivePossibleMoves(Arrays.asList(
                Collections.singletonList("a2a4"),
                Collections.singletonList("a7a5"),
                Collections.emptyList()
        ));
        List<String> moveList = Arrays.asList("a2a4", "a7a5", "b2b4");
        assertThrows(IllegalMoveException.class,
                () -> this.subject.move(moveList));

        verify(this.process, times(1)).writeCommand("position fen " +
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 " +
                "moves a2a4");
        verify(this.process, times(1)).writeCommand("position fen " +
                "rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1 " +
                "moves a7a5");
        verify(this.process, times(1)).writeCommand("position fen " +
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    public void shouldGoBackToInitialPositionIfTimeoutDuringProcess() throws IOException, TimeoutException {
        initStockfishInstance(true);
        this.subject.setSuccessiveFens(Collections.singletonList(
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        ));
        this.subject.setSuccessivePossibleMoves(Collections.singletonList(
                Collections.singletonList("a2a4")
        ));
        when(this.process.readLinesUntil(any(Pattern.class), anyInt())).thenThrow(TimeoutException.class);
        assertThrows(TimeoutException.class,
                () -> this.subject.move(Collections.singletonList("a2a4")));

        verify(this.process, times(1)).writeCommand("position fen " +
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    public void shouldThrowExceptionIfOneOnTheMovesHasInvalidSyntax() throws IOException, TimeoutException {
        initStockfishInstance(true);
        List<String> moveList = Arrays.asList("a2a4", "a8a9");
        assertThrows(InvalidMoveSyntaxException.class,
                () -> this.subject.move(moveList));
    }

    @Test
    public void shouldGetBestMove() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs" +
                        "/goDepth10InitialPosition.txt",
                "go depth 10");
        assertEquals("e2e4",
                this.subject.findBestMove(EvaluationParameters.builder().depth(10).build()));
    }

    @Test
    public void shouldGetBestMoveReturnsNullIfEndGame() throws IOException,
            TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/goDepth10EndGame" +
                        ".txt",
                "go depth 10");
        assertNull(this.subject.findBestMove(EvaluationParameters.builder().depth(10).build()));
    }

    @Test
    public void shouldGetCentipawnsEvaluation() throws IOException,
            TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs" +
                        "/centipawnEvaluation.txt",
                "go depth 10");
        this.subject.setSuccessiveFens(Collections.singletonList("rnbqkbnr" +
                "/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        assertEquals(
                new ChessEngineEvaluation(ChessEngineEvaluationType.CENTIPAWNS, 105),
                this.subject.getPositionEvaluation(EvaluationParameters.builder().depth(10).build())
        );
    }

    @Test
    public void shouldGetMateEvaluation() throws IOException, TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/mateEvaluation.txt",
                "go depth 10");
        this.subject.setSuccessiveFens(Collections.singletonList("rnbqkbnr" +
                "/1ppppppp/8/8/2B1P3/p4Q2/PPPP1PPP/RNB1K1NR w KQkq - 0 4"));
        assertEquals(
                new ChessEngineEvaluation(ChessEngineEvaluationType.MATE, 1),
                this.subject.getPositionEvaluation(EvaluationParameters.builder().depth(10).build())
        );
    }

    @Test
    public void shouldGetEvaluationForBlack() throws IOException,
            TimeoutException {
        initStockfishInstance(true);
        bindFileToLineReaderWhenWriting("stockfish/outputs/mateEvaluation.txt",
                "go depth 10");
        this.subject.setSuccessiveFens(Collections.singletonList("rnbqkbnr" +
                "/1ppppppp/8/8/2B1P3/p4Q2/PPPP1PPP/RNB1K1NR b KQkq - 0 4"));
        assertEquals(
                new ChessEngineEvaluation(ChessEngineEvaluationType.MATE, -1),
                this.subject.getPositionEvaluation(EvaluationParameters.builder().depth(10).build())
        );
    }

    @Test
    public void shouldReturnTrueIfHealthcheckNoException() throws IOException
            , TimeoutException {
        initStockfishInstance(true);
        assertTrue(this.subject.healthCheck());
    }

    @Test
    public void shouldReturnFalseIfHealthcheckWithException() throws IOException, TimeoutException {
        initStockfishInstance(true);
        this.subject.setWaitUntilReadyThrowException();
        assertFalse(this.subject.healthCheck());
    }

    private void initStockfishInstance(boolean validInitOutput) throws IOException, TimeoutException {
        String file = validInitOutput ? "stockfish/outputs/init.txt" :
                "stockfish/outputs/invalidInit.txt";
        this.bindFileToLineReaderWhenWriting(file, "isready");
        this.subject = new StockfishTestImpl(process, config);
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
        when(this.process.readLinesUntil(anyString(),
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

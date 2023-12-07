package stockfish.option;

import com.xchess.stockfish.option.StockfishOptions;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

public class StockfishOptionsTest {
    @Test
    public void shouldBuildAllCommands() {
        StockfishOptions so = StockfishOptions.getDefaultOptions();

        assertEquals(
                Arrays.asList(
                        "setoption name Threads value 1",
                        "setoption name Hash value 16",
                        "setoption name Ponder value false",
                        "setoption name Move Overhead value 10",
                        "setoption name Skill Level value 20",
                        "setoption name Debug Log File value ",
                        "setoption name Slow Mover value 100",
                        "setoption name UCI_Chess960 value false",
                        "setoption name UCI_Elo value 1320",
                        "setoption name UCI_LimitStrength value false"
                ),
                so.getCommands()
        );
    }

    @Test
    public void mergeShouldOverrideExistingAndAddNew() {
        StockfishOptions base = StockfishOptions.builder()
                .hash(12)
                .threads(12)
                .build();
        StockfishOptions other = StockfishOptions.builder()
                .threads(25)
                .debugLogFile("file.txt")
                .build();
        StockfishOptions result = base.merge(other);

        assertEquals(
                Arrays.asList(
                        "setoption name Threads value 25",
                        "setoption name Hash value 12",
                        "setoption name Debug Log File value file.txt"
                ),
                result.getCommands()
        );
    }
}

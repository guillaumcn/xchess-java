package com.xchess.stockfish.option;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stockfish engine options
 * For more information, see Stockfish engine complete documentation
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockfishOptions {
    private Integer threads;
    private Integer hash;
    private Boolean ponder;
    private Integer moveOverhead;
    private Integer skillLevel;
    private String debugLogFile;
    private Integer slowMover;
    private Boolean uciChess960;
    private Integer uciElo;
    private Boolean uciLimitStrength;

    public StockfishOptions(StockfishOptions other) {
        this.threads = other.threads;
        this.hash = other.hash;
        this.ponder = other.ponder;
        this.moveOverhead = other.moveOverhead;
        this.skillLevel = other.skillLevel;
        this.debugLogFile = other.debugLogFile;
        this.slowMover = other.slowMover;
        this.uciChess960 = other.uciChess960;
        this.uciElo = other.uciElo;
        this.uciLimitStrength = other.uciLimitStrength;
    }

    /**
     * Merge two options.
     * This method will not update current option object but return a new one
     * . If both options have the same parameter, the given
     * option parameter will be kept
     *
     * @param other the Stockfish option to merge with
     * @return The merged options object
     */
    public StockfishOptions merge(StockfishOptions other) {
        StockfishOptions result = new StockfishOptions(this);
        result.threads = Objects.isNull(other.threads) ? this.threads :
                other.threads;
        result.hash = Objects.isNull(other.hash) ? this.hash :
                other.hash;
        result.ponder = Objects.isNull(other.ponder) ? this.ponder :
                other.ponder;
        result.moveOverhead = Objects.isNull(other.moveOverhead) ?
                this.moveOverhead :
                other.moveOverhead;
        result.skillLevel = Objects.isNull(other.skillLevel) ? this.skillLevel :
                other.skillLevel;
        result.debugLogFile = Objects.isNull(other.debugLogFile) ?
                this.debugLogFile :
                other.debugLogFile;
        result.slowMover = Objects.isNull(other.slowMover) ? this.slowMover :
                other.slowMover;
        result.uciChess960 = Objects.isNull(other.uciChess960) ?
                this.uciChess960 :
                other.uciChess960;
        result.uciElo = Objects.isNull(other.uciElo) ? this.uciElo :
                other.uciElo;
        result.uciLimitStrength = Objects.isNull(other.uciLimitStrength) ?
                this.uciLimitStrength :
                other.uciLimitStrength;
        return result;
    }

    /**
     * Get a list of commands that will be sent to Stockfish engine
     *
     * @return The list of commands
     */
    public List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        String nameBinding = "{{name}}";
        String valueBinding = "{{value}}";
        String commandTemplate =
                "setoption name " + nameBinding + " value " + valueBinding;
        if (!Objects.isNull(this.threads)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "Threads").
                    replace(valueBinding, this.threads.toString()));
        }
        if (!Objects.isNull(this.hash)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "Hash").
                    replace(valueBinding, this.hash.toString()));
        }
        if (!Objects.isNull(this.ponder)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "Ponder").
                    replace(valueBinding, this.ponder.toString()));
        }
        if (!Objects.isNull(this.moveOverhead)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "Move Overhead").
                    replace(valueBinding, this.moveOverhead.toString()));
        }
        if (!Objects.isNull(this.skillLevel)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "Skill Level").
                    replace(valueBinding, this.skillLevel.toString()));
        }
        if (!Objects.isNull(this.debugLogFile)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "Debug Log File").
                    replace(valueBinding, this.debugLogFile));
        }
        if (!Objects.isNull(this.slowMover)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "Slow Mover").
                    replace(valueBinding, this.slowMover.toString()));
        }
        if (!Objects.isNull(this.uciChess960)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "UCI_Chess960").
                    replace(valueBinding, this.uciChess960.toString()));
        }
        if (!Objects.isNull(this.uciElo)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "UCI_Elo").
                    replace(valueBinding, this.uciElo.toString()));
        }
        if (!Objects.isNull(this.uciLimitStrength)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "UCI_LimitStrength").
                    replace(valueBinding, this.uciLimitStrength.toString()));
        }
        return commands;
    }

    /**
     * Get the default options as they are set on Stockfish engine. These
     * options values will be set at startup
     *
     * @return the Stockfish options object
     */
    public static StockfishOptions getDefaultOptions() {
        return StockfishOptions.builder()
                .threads(1)
                .hash(16)
                .ponder(false)
                .moveOverhead(10)
                .skillLevel(20)
                .debugLogFile("")
                .slowMover(100)
                .uciChess960(false)
                .uciElo(1320)
                .uciLimitStrength(false)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockfishOptions that = (StockfishOptions) o;
        return Objects.equals(threads, that.threads) && Objects.equals(hash,
                that.hash) && Objects.equals(ponder, that.ponder) && Objects.equals(moveOverhead, that.moveOverhead) && Objects.equals(skillLevel, that.skillLevel) && Objects.equals(debugLogFile, that.debugLogFile) && Objects.equals(slowMover, that.slowMover) && Objects.equals(uciChess960, that.uciChess960) && Objects.equals(uciElo, that.uciElo) && Objects.equals(uciLimitStrength, that.uciLimitStrength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(threads, hash, ponder, moveOverhead,
                skillLevel, debugLogFile, slowMover, uciChess960, uciElo,
                uciLimitStrength);
    }

    @Override
    public String toString() {
        return "StockfishOptions{" +
                "threads=" + threads +
                ", hash=" + hash +
                ", ponder=" + ponder +
                ", moveOverhead=" + moveOverhead +
                ", skillLevel=" + skillLevel +
                ", debugLogFile='" + debugLogFile + '\'' +
                ", slowMover=" + slowMover +
                ", uciChess960=" + uciChess960 +
                ", uciElo=" + uciElo +
                ", uciLimitStrength=" + uciLimitStrength +
                '}';
    }
}

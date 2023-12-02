package com.xchess.stockfish.option;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stockfish engine options
 * For more information, see Stockfish engine complete documentation
 */
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

    public StockfishOptions() {
    }

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
     * @param threads The number of CPU threads used for searching a position
     *                . For best performance, set this equal to the number of
     *                CPU cores available
     * @return the options object
     */
    public StockfishOptions setThreads(Integer threads) {
        this.threads = threads;
        return this;
    }

    /**
     * @param hash The size of the hash table in MB. It is recommended to set
     *             Hash after setting Threads
     * @return the options object
     */
    public StockfishOptions setHash(Integer hash) {
        this.hash = hash;
        return this;
    }

    /**
     * Let Stockfish ponder its next move while the opponent is thinking
     *
     * @param ponder the boolean value
     * @return the options object
     */
    public StockfishOptions setPonder(Boolean ponder) {
        this.ponder = ponder;
        return this;
    }

    /**
     * Assume a time delay of x ms due to network and GUI overheads.
     * Specifying a value larger than the default is needed to avoid time
     * losses or near instantaneous moves, in particular for time controls
     * without increment (e.g. sudden death). The default is suitable for
     * engine-engine matches played locally on dedicated hardware, while it
     * needs to be increased on a loaded system, when playing over a network,
     * or when using certain GUIs such as Arena or ChessGUI
     *
     * @param moveOverhead the delay in ms
     * @return the options object
     */
    public StockfishOptions setMoveOverhead(Integer moveOverhead) {
        this.moveOverhead = moveOverhead;
        return this;
    }

    /**
     * Lower the Skill Level in order to make Stockfish play weaker (see also
     * UCI_LimitStrength). Internally, MultiPV is enabled, and with a certain
     * probability depending on the Skill Level, a weaker move will be played.
     *
     * @param skillLevel the skill level
     * @return the options object
     */
    public StockfishOptions setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
        return this;
    }

    /**
     * Write all communication to and from the engine into a text file
     *
     * @param debugLogFile the file path
     * @return the options object
     */
    public StockfishOptions setDebugLogFile(String debugLogFile) {
        this.debugLogFile = debugLogFile;
        return this;
    }

    /**
     * Lower values will make Stockfish take less time in games, higher
     * values will make it think longer
     *
     * @param slowMover the value
     * @return the options object
     */
    public StockfishOptions setSlowMover(Integer slowMover) {
        this.slowMover = slowMover;
        return this;
    }

    /**
     * An option handled by your GUI. If true, Stockfish will play Chess960
     *
     * @param uciChess960 activated or not
     * @return the options object
     */
    public StockfishOptions setUciChess960(Boolean uciChess960) {
        this.uciChess960 = uciChess960;
        return this;
    }

    /**
     * If enabled by UCI_LimitStrength, aim for an engine strength of the
     * given Elo. This Elo rating has been calibrated at a time control of
     * 60s+0.6s and anchored to CCRL 40/4
     *
     * @param uciElo the elo value
     * @return the options object
     */
    public StockfishOptions setUciElo(Integer uciElo) {
        this.uciElo = uciElo;
        return this;
    }

    /**
     * Enable weaker play aiming for an Elo rating as set by UCI_Elo. This
     * option overrides Skill Level
     *
     * @param uciLimitStrength activated or not
     * @return the options object
     */
    public StockfishOptions setUciLimitStrength(Boolean uciLimitStrength) {
        this.uciLimitStrength = uciLimitStrength;
        return this;
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
        return new StockfishOptions()
                .setThreads(1)
                .setHash(16)
                .setPonder(false)
                .setMoveOverhead(10)
                .setSkillLevel(20)
                .setDebugLogFile("")
                .setSlowMover(100)
                .setUciChess960(false)
                .setUciElo(1320)
                .setUciLimitStrength(false);
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

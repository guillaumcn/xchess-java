package com.xchess.stockfish.option;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StockfishOptions {
    private Integer threads;
    private Integer hash;
    private Boolean ponder;
    private Integer moveOverhead;
    private Integer multiPv;
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
        this.multiPv = other.multiPv;
        this.skillLevel = other.skillLevel;
        this.debugLogFile = other.debugLogFile;
        this.slowMover = other.slowMover;
        this.uciChess960 = other.uciChess960;
        this.uciElo = other.uciElo;
        this.uciLimitStrength = other.uciLimitStrength;
    }

    public StockfishOptions setThreads(Integer threads) {
        this.threads = threads;
        return this;
    }

    public StockfishOptions setHash(Integer hash) {
        this.hash = hash;
        return this;
    }

    public StockfishOptions setPonder(Boolean ponder) {
        this.ponder = ponder;
        return this;
    }

    public StockfishOptions setMoveOverhead(Integer moveOverhead) {
        this.moveOverhead = moveOverhead;
        return this;
    }

    public StockfishOptions setMultiPv(Integer multiPv) {
        this.multiPv = multiPv;
        return this;
    }

    public StockfishOptions setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
        return this;
    }

    public StockfishOptions setDebugLogFile(String debugLogFile) {
        this.debugLogFile = debugLogFile;
        return this;
    }

    public StockfishOptions setSlowMover(Integer slowMover) {
        this.slowMover = slowMover;
        return this;
    }

    public StockfishOptions setUciChess960(Boolean uciChess960) {
        this.uciChess960 = uciChess960;
        return this;
    }

    public StockfishOptions setUciElo(Integer uciElo) {
        this.uciElo = uciElo;
        return this;
    }

    public StockfishOptions setUciLimitStrength(Boolean uciLimitStrength) {
        this.uciLimitStrength = uciLimitStrength;
        return this;
    }

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
        result.multiPv = Objects.isNull(other.multiPv) ? this.multiPv :
                other.multiPv;
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

    public List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        String nameBinding = "{{name}}";
        String valueBinding = "{{value}}";
        String commandTemplate = "setoption name "+ nameBinding + " value " + valueBinding;
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
        if (!Objects.isNull(this.multiPv)) {
            commands.add(commandTemplate.
                    replace(nameBinding, "MultiPV").
                    replace(valueBinding, this.multiPv.toString()));
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

    public static StockfishOptions getDefaultOptions() {
        return new StockfishOptions()
                .setThreads(1)
                .setHash(16)
                .setPonder(false)
                .setMoveOverhead(10)
                .setMultiPv(1)
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
        return Objects.equals(threads, that.threads) && Objects.equals(hash, that.hash) && Objects.equals(ponder, that.ponder) && Objects.equals(moveOverhead, that.moveOverhead) && Objects.equals(multiPv, that.multiPv) && Objects.equals(skillLevel, that.skillLevel) && Objects.equals(debugLogFile, that.debugLogFile) && Objects.equals(slowMover, that.slowMover) && Objects.equals(uciChess960, that.uciChess960) && Objects.equals(uciElo, that.uciElo) && Objects.equals(uciLimitStrength, that.uciLimitStrength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(threads, hash, ponder, moveOverhead, multiPv, skillLevel, debugLogFile, slowMover, uciChess960, uciElo, uciLimitStrength);
    }
}

package com.xchess.main;

import java.util.List;
import java.util.Objects;

public class StockfishOptions {
    private Integer threads;
    private Integer hash;
    private Boolean ponder;
    private Integer multiPv;
    private String debugLogFile;
    private Integer skillLevel;
    private Integer moveOverhead;
    private Integer slowMover;
    private Boolean UCIChess960;
    private Boolean UCILimitStrength;
    private Boolean UCiElo;

    public StockfishOptions() {
    }

    public Integer getThreads() {
        return threads;
    }

    public StockfishOptions setThreads(Integer threads) {
        this.threads = threads;
        return this;
    }

    public Integer getHash() {
        return hash;
    }

    public StockfishOptions setHash(Integer hash) {
        this.hash = hash;
        return this;
    }

    public Boolean getPonder() {
        return ponder;
    }

    public StockfishOptions setPonder(Boolean ponder) {
        this.ponder = ponder;
        return this;
    }

    public Integer getMultiPv() {
        return multiPv;
    }

    public StockfishOptions setMultiPv(Integer multiPv) {
        this.multiPv = multiPv;
        return this;
    }

    public String getDebugLogFile() {
        return debugLogFile;
    }

    public StockfishOptions setDebugLogFile(String debugLogFile) {
        this.debugLogFile = debugLogFile;
        return this;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }

    public StockfishOptions setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
        return this;
    }

    public Integer getMoveOverhead() {
        return moveOverhead;
    }

    public StockfishOptions setMoveOverhead(Integer moveOverhead) {
        this.moveOverhead = moveOverhead;
        return this;
    }

    public Integer getSlowMover() {
        return slowMover;
    }

    public StockfishOptions setSlowMover(Integer slowMover) {
        this.slowMover = slowMover;
        return this;
    }

    public Boolean getUCIChess960() {
        return UCIChess960;
    }

    public StockfishOptions setUCIChess960(Boolean UCIChess960) {
        this.UCIChess960 = UCIChess960;
        return this;
    }

    public Boolean getUCILimitStrength() {
        return UCILimitStrength;
    }

    public StockfishOptions setUCILimitStrength(Boolean UCILimitStrength) {
        this.UCILimitStrength = UCILimitStrength;
        return this;
    }

    public Boolean getUCiElo() {
        return UCiElo;
    }

    public StockfishOptions setUCiElo(Boolean UCiElo) {
        this.UCiElo = UCiElo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockfishOptions that = (StockfishOptions) o;
        return threads == that.threads && hash == that.hash && ponder == that.ponder && multiPv == that.multiPv && skillLevel == that.skillLevel && moveOverhead == that.moveOverhead && slowMover == that.slowMover && UCIChess960 == that.UCIChess960 && UCILimitStrength == that.UCILimitStrength && UCiElo == that.UCiElo && Objects.equals(debugLogFile, that.debugLogFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(threads, hash, ponder, multiPv, debugLogFile, skillLevel, moveOverhead, slowMover, UCIChess960, UCILimitStrength, UCiElo);
    }

    @Override
    public StockfishOptions clone() throws CloneNotSupportedException {
        StockfishOptions clone = (StockfishOptions) super.clone();
        return clone
                .setThreads(this.getThreads())
                .setHash(this.getHash())
                .setPonder(this.getPonder())
                .setMoveOverhead(this.getMoveOverhead())
                .setMultiPv(this.getMultiPv())
                .setSkillLevel(this.getSkillLevel())
                .setDebugLogFile(this.getDebugLogFile())
                .setSlowMover(this.getSlowMover())
                .setUCIChess960(this.getUCIChess960())
                .setUCiElo(this.getUCiElo())
                .setUCILimitStrength(this.getUCILimitStrength());
    }

    public StockfishOptions merge(StockfishOptions other) throws CloneNotSupportedException {
        StockfishOptions result = this.clone();
        if (!Objects.isNull(other.getThreads())) {
            result = result.setThreads(other.getThreads());
        }
        if (!Objects.isNull(other.getHash())) {
            result = result.setHash(other.getHash());
        }
        if (!Objects.isNull(other.getPonder())) {
            result = result.setPonder(other.getPonder());
        }
        if (!Objects.isNull(other.getMoveOverhead())) {
            result = result.setMoveOverhead(other.getMoveOverhead());
        }
        if (!Objects.isNull(other.getMultiPv())) {
            result = result.setMultiPv(other.getMultiPv());
        }
        if (!Objects.isNull(other.getSkillLevel())) {
            result = result.setSkillLevel(other.getSkillLevel());
        }
        if (!Objects.isNull(other.getDebugLogFile())) {
            result = result.setDebugLogFile(other.getDebugLogFile());
        }
        if (!Objects.isNull(other.getSlowMover())) {
            result = result.setSlowMover(other.getSlowMover());
        }
        if (!Objects.isNull(other.getUCIChess960())) {
            result = result.setUCIChess960(other.getUCIChess960());
        }
        if (!Objects.isNull(other.getUCiElo())) {
            result = result.setUCiElo(other.getUCiElo());
        }
        if (!Objects.isNull(other.getUCILimitStrength())) {
            result = result.setUCILimitStrength(other.getUCILimitStrength());
        }

        return result;
    }

    public List<String> getCommandStrings() {

    }
}

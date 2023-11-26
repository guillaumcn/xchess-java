package com.xchess.stockfish.command;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class BestmoveCommandBuilder {
    private List<String> searchMoves;
    private Integer wtime;
    private Integer btime;
    private Integer winc;
    private Integer binc;
    private Integer movestogo;
    private Integer depth;
    private Integer nodes;
    private Integer mate;
    private Integer movetime;

    public BestmoveCommandBuilder() {
    }

    public BestmoveCommandBuilder setSearchMoves(List<String> searchMoves) {
        this.searchMoves = searchMoves;
        return this;
    }

    public BestmoveCommandBuilder setWtime(Integer wtime) {
        this.wtime = wtime;
        return this;
    }

    public BestmoveCommandBuilder setBtime(Integer btime) {
        this.btime = btime;
        return this;
    }

    public BestmoveCommandBuilder setWinc(Integer winc) {
        this.winc = winc;
        return this;
    }

    public BestmoveCommandBuilder setBinc(Integer binc) {
        this.binc = binc;
        return this;
    }

    public BestmoveCommandBuilder setMovestogo(Integer movestogo) {
        this.movestogo = movestogo;
        return this;
    }

    public BestmoveCommandBuilder setDepth(Integer depth) {
        this.depth = depth;
        return this;
    }

    public BestmoveCommandBuilder setNodes(Integer nodes) {
        this.nodes = nodes;
        return this;
    }

    public BestmoveCommandBuilder setMate(Integer mate) {
        this.mate = mate;
        return this;
    }

    public BestmoveCommandBuilder setMovetime(Integer movetime) {
        this.movetime = movetime;
        return this;
    }

    public String build() {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("go");
        if (!Objects.isNull(searchMoves) && !searchMoves.isEmpty()) {
            joiner.add("searchmoves " + String.join(" ", searchMoves));
        }
        if (!Objects.isNull(wtime)) {
            joiner.add("wtime " + wtime);
        }
        if (!Objects.isNull(btime)) {
            joiner.add("btime " + btime);
        }
        if (!Objects.isNull(winc)) {
            joiner.add("winc " + winc);
        }
        if (!Objects.isNull(binc)) {
            joiner.add("binc " + binc);
        }
        if (!Objects.isNull(movestogo)) {
            joiner.add("movestogo " + movestogo);
        }
        if (!Objects.isNull(depth)) {
            joiner.add("depth " + depth);
        }
        if (!Objects.isNull(nodes)) {
            joiner.add("nodes " + nodes);
        }
        if (!Objects.isNull(mate)) {
            joiner.add("mate " + mate);
        }
        if (!Objects.isNull(movetime)) {
            joiner.add("movetime " + movetime);
        }
        return joiner.toString();
    }
}

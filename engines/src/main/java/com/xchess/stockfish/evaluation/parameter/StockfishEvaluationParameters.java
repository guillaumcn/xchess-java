package com.xchess.stockfish.evaluation.parameter;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Parameters for a Stockfish evaluation of type "go ..."
 */
public class StockfishEvaluationParameters {
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

    /**
     * Restrict search to these moves only
     *
     * @param searchMoves List of moves
     * @return the parameter object
     */
    public StockfishEvaluationParameters setSearchMoves(List<String> searchMoves) {
        this.searchMoves = searchMoves;
        return this;
    }

    /**
     * White has x ms left on the clock
     *
     * @param wtime the time left in ms
     * @return the parameter object
     */
    public StockfishEvaluationParameters setWtime(Integer wtime) {
        this.wtime = wtime;
        return this;
    }

    /**
     * Black has x ms left on the clock
     *
     * @param btime the time left in ms
     * @return the parameter object
     */
    public StockfishEvaluationParameters setBtime(Integer btime) {
        this.btime = btime;
        return this;
    }

    /**
     * White increment per move in ms if x > 0
     *
     * @param winc the increment in ms
     * @return the parameter object
     */
    public StockfishEvaluationParameters setWinc(Integer winc) {
        this.winc = winc;
        return this;
    }

    /**
     * Black increment per move in ms if x > 0
     *
     * @param binc the increment in ms
     * @return the parameter object
     */
    public StockfishEvaluationParameters setBinc(Integer binc) {
        this.binc = binc;
        return this;
    }

    /**
     * There are x moves to the next time control
     *
     * @param movestogo number of moves
     * @return the parameter object
     */
    public StockfishEvaluationParameters setMovestogo(Integer movestogo) {
        this.movestogo = movestogo;
        return this;
    }

    /**
     * Search x plies only
     *
     * @param depth the number of piles
     * @return the parameter object
     */
    public StockfishEvaluationParameters setDepth(Integer depth) {
        this.depth = depth;
        return this;
    }

    /**
     * Search x nodes only
     *
     * @param nodes The number of nodes
     * @return the parameter object
     */
    public StockfishEvaluationParameters setNodes(Integer nodes) {
        this.nodes = nodes;
        return this;
    }

    /**
     * Search for a mate in x moves
     *
     * @param mate the number of moves
     * @return the parameter object
     */
    public StockfishEvaluationParameters setMate(Integer mate) {
        this.mate = mate;
        return this;
    }

    /**
     * Search exactly x ms
     *
     * @param movetime the time in ms to search. Should be lower than timeout
     *                 config of stockfish engine integration
     * @return the parameter object
     */
    public StockfishEvaluationParameters setMovetime(Integer movetime) {
        this.movetime = movetime;
        return this;
    }

    /**
     * @return The command to write to Stockfish engine
     */
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

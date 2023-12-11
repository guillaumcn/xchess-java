package com.xchess.evaluation.parameter;

import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationParameters {
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
     * @return The command to write to Stockfish engine
     */
    public String buildCommand() {
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

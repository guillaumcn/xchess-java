package com.xchess.stockfish.option;

public enum StockfishOptionKey {
    THREADS("Threads"),
    HASH("Hash"),
    PONDER("Ponder"),
    MOVE_OVERHEAD("Move Overhead"),
    MULTIPV("MultiPV"),
    SKILL_LEVEL("Skill Level"),
    DEBUG_LOG_FILE("Debug Log File"),
    SLOW_MOVER("Slow Mover"),
    UCI_CHESS960("UCI_Chess960"),
    UCI_ELO("UCI_Elo"),
    UCI_LIMITSTRENGTH("UCI_LimitStrength");

    private final String value;

    StockfishOptionKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

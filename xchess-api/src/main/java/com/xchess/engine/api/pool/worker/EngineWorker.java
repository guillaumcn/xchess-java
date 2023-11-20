package com.xchess.engine.api.pool.worker;

import java.io.IOException;
import java.util.List;

public interface EngineWorker {
    void start() throws Exception;

    void stop() throws IOException;

    boolean isValid();

    List<String> getPossibleMoves(String fen);
}

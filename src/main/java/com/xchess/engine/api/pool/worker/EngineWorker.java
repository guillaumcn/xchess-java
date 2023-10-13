package com.xchess.engine.api.pool.worker;

import java.io.IOException;

public interface EngineWorker {
    void start() throws Exception;

    void runCommand(String command) throws IOException;

    void stop();
}

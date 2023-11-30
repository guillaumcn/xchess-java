package com.xchess.engine.api.controller;

import com.xchess.ChessEngine;
import com.xchess.engine.api.pool.PoolWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
public class TestController {

    private final PoolWrapper poolWrapper;

    @Autowired
    public TestController(PoolWrapper poolWrapper) {
        this.poolWrapper = poolWrapper;
    }

    @GetMapping(value = "/possibleMovesFromStart")
    public List<String> getOne() throws Exception {
        return this.poolWrapper.doAction(engineWorker -> {
            try {
                engineWorker.moveToFenPosition("rnbqkbnr/pppppppp/8/8/6P1/8" +
                        "/PPPPPP1P/RNBQKBNR b KQkq - 0 1");
                return engineWorker.getPossibleMoves();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @GetMapping(value = "/getEngineVersion")
    public float getEngineVersion() throws Exception {
        return this.poolWrapper.doAction(ChessEngine::getEngineVersion);
    }
}

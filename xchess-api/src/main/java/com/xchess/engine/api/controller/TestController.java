package com.xchess.engine.api.controller;

import com.xchess.engine.api.pool.PoolWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private PoolWrapper poolWrapper;

    @Autowired
    public TestController(PoolWrapper poolWrapper) {
        this.poolWrapper = poolWrapper;
    }

    @GetMapping(value = "/possibleMovesFromStart")
    public List<String> getOne() throws Exception {
        return this.poolWrapper.doAction((engineWorker) -> engineWorker.getPossibleMoves("rnbqkbnr/pppppppp/8/8/6P1/8/PPPPPP1P/RNBQKBNR b KQkq - 0 1"));
    }
}

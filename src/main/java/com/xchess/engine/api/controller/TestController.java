package com.xchess.engine.api.controller;

import com.xchess.engine.api.pool.PoolWrapper;
import com.xchess.engine.api.pool.worker.EngineWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    List<EngineWorker> workers;

    private PoolWrapper poolWrapper;

    @Autowired
    public TestController(PoolWrapper poolWrapper) {
        this.poolWrapper = poolWrapper;
        workers = new ArrayList<>();
    }

    @GetMapping(value = "/get")
    public void getOne() throws Exception {
        EngineWorker engineWorker = this.poolWrapper.getPool().borrowObject();
        workers.add(engineWorker);
    }

    @GetMapping(value = "/release")
    public void releaseOne() {
        this.poolWrapper.getPool().returnObject(workers.remove(0));
    }
}

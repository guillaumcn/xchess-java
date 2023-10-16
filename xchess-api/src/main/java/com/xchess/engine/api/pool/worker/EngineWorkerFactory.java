package com.xchess.engine.api.pool.worker;

import com.xchess.engine.api.pool.worker.impl.EngineWorkerStockfishImpl;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class EngineWorkerFactory extends BasePooledObjectFactory<EngineWorker> {

    @Override
    public EngineWorker create() throws Exception {
        EngineWorkerStockfishImpl engineWorker = new EngineWorkerStockfishImpl();
        engineWorker.start();
        return engineWorker;
    }

    @Override
    public PooledObject<EngineWorker> wrap(EngineWorker engineWorker) {
        return new DefaultPooledObject<>(engineWorker);
    }

    @Override
    public void destroyObject(PooledObject<EngineWorker> p, DestroyMode destroyMode) throws Exception {
        super.destroyObject(p, destroyMode);
        EngineWorker engineWorker = p.getObject();
        engineWorker.stop();
    }
}

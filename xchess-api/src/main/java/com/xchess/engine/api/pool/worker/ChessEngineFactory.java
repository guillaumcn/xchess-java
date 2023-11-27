package com.xchess.engine.api.pool.worker;

import com.xchess.ChessEngine;
import com.xchess.stockfish.Stockfish;
import com.xchess.stockfish.config.StockfishConfig;
import com.xchess.stockfish.process.ProcessWrapper;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ChessEngineFactory extends BasePooledObjectFactory<ChessEngine> {

    @Override
    public ChessEngine create() throws Exception {
        return new Stockfish(new ProcessWrapper("stockfish"),
                new StockfishConfig().setTimeoutInMs(1000));
    }

    @Override
    public PooledObject<ChessEngine> wrap(ChessEngine engineWorker) {
        return new DefaultPooledObject<>(engineWorker);
    }

    @Override
    public void destroyObject(PooledObject<ChessEngine> p,
                              DestroyMode destroyMode) throws Exception {
        super.destroyObject(p, destroyMode);
        ChessEngine chessEngine = p.getObject();
        chessEngine.stop();
    }

    @Override
    public boolean validateObject(PooledObject<ChessEngine> p) {
        return p.getObject().healthCheck();
    }
}

package com.xchess.engine.api.pool;

import com.xchess.engine.api.pool.worker.EngineWorker;
import com.xchess.engine.api.pool.worker.EngineWorkerFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Function;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PoolWrapper {
    private final GenericObjectPool<EngineWorker> pool;

    @Autowired
    public PoolWrapper(PoolProperties poolProperties) throws Exception {
        GenericObjectPoolConfig<EngineWorker> config = new GenericObjectPoolConfig<>();
        config.setMinIdle(poolProperties.getMinIdle());
        config.setMaxTotal(poolProperties.getMaxTotal());
        config.setSoftMinEvictableIdleDuration(Duration.ofMillis(poolProperties.getEvictableIdleDurationInMs()));
        config.setTimeBetweenEvictionRuns(Duration.ofMillis(poolProperties.getTimeBetweenEvictionRunsInMs()));

        this.pool = new GenericObjectPool<>(new EngineWorkerFactory(), config);
        this.pool.preparePool();
    }

    public <T> T doAction(Function<EngineWorker, T> action) throws Exception {
        EngineWorker engineWorker = this.pool.borrowObject();
        try {
            T result = action.apply(engineWorker);
            this.pool.returnObject(engineWorker);
            return result;
        } catch (Exception e) {
            this.pool.invalidateObject(engineWorker);
            throw e;
        }
    }
}

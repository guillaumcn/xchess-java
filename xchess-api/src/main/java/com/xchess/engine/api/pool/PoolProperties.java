package com.xchess.engine.api.pool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pool.engine")
public class PoolProperties {
    private int minIdle;
    private int maxTotal;
    private int timeBetweenEvictionRunsInMs;
    private int evictableIdleDurationInMs;

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getTimeBetweenEvictionRunsInMs() {
        return timeBetweenEvictionRunsInMs;
    }

    public int getEvictableIdleDurationInMs() {
        return evictableIdleDurationInMs;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setTimeBetweenEvictionRunsInMs(int timeBetweenEvictionRunsInMs) {
        this.timeBetweenEvictionRunsInMs = timeBetweenEvictionRunsInMs;
    }

    public void setEvictableIdleDurationInMs(int evictableIdleDurationInMs) {
        this.evictableIdleDurationInMs = evictableIdleDurationInMs;
    }
}

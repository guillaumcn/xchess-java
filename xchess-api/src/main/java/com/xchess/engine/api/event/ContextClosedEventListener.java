package com.xchess.engine.api.event;

import com.xchess.engine.api.pool.PoolWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ContextClosedEventListener {
    private PoolWrapper poolWrapper;

    @Autowired
    public ContextClosedEventListener(PoolWrapper poolWrapper) {
        this.poolWrapper = poolWrapper;
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent contextClosedEvent) {
        this.poolWrapper.getPool().close();
    }
}

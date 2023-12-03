package ru.tocorin.wyrm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class WyrmThreadFactory implements ThreadFactory {
    private static final Logger log = LoggerFactory.getLogger(WyrmThreadFactory.class);
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("wyrm-thread-" + counter.getAndIncrement());
        thread.setUncaughtExceptionHandler((t, e) -> log.error("{}", t, e));
        log.info("create a new thread - {}", thread.getName());
        return thread;
    }
}

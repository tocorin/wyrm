package ru.tocorin.wyrm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class WyrmRejectHandler  implements RejectedExecutionHandler {
    private static final Logger log = LoggerFactory.getLogger(WyrmRejectHandler.class);
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.info("rejected task - {}", r);
    }
}

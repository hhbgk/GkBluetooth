package com.gk.lib.bluetooth.engine;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Des:
 * Author: hhbgk
 * Date:20-5-8
 * UpdateRemark:
 */
public final class ThreadPool {
    private Executor taskExecutor;

    public ThreadPool() {
        taskExecutor = Executors.newFixedThreadPool(3);
    }

    public void submit(Runnable runnable) {
        taskExecutor.execute(runnable);
    }

    public void stop() {
        ((ExecutorService) taskExecutor).shutdownNow();
    }
}

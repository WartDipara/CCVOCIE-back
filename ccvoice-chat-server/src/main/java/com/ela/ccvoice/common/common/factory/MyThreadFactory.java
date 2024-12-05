package com.ela.ccvoice.common.common.factory;

import com.ela.ccvoice.common.common.handler.GlobalUncaughtExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;

@AllArgsConstructor
@Slf4j
public class MyThreadFactory implements ThreadFactory {
    private final ThreadFactory factory;
    @Override
    public Thread newThread(Runnable r) {
        Thread thread  = factory.newThread(r);
        //自定义异常逻辑
        thread.setUncaughtExceptionHandler(GlobalUncaughtExceptionHandler.getInstance());
        return thread;
    }
}

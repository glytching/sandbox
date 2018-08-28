package org.glytching.sandbox.threadlocal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalSUT {
    public static final ThreadLocal<ThreadLocalSUT> REQUEST_HOLDER = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalSUT.class);

    public static void doSomeMethodHere() {
        ThreadLocalSUT request = ThreadLocalSUT.REQUEST_HOLDER.get();
        logger.info("Current thread id: {}", Thread.currentThread().getId());
        if (request == null) {
            logger.info("Request is null");
        }
    }

}

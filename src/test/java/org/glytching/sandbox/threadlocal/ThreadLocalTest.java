package org.glytching.sandbox.threadlocal;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ThreadLocalTest {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalTest.class);

    @Test
    public void test() {
        ThreadLocalSUT mockedRequest = new ThreadLocalSUT();
        ThreadLocalSUT.REQUEST_HOLDER.set(mockedRequest);

        ThreadLocalSUT.doSomeMethodHere();

        assertNotNull(ThreadLocalSUT.REQUEST_HOLDER.get());
        logger.info("Current thread id: {}", Thread.currentThread().getId());
    }

}
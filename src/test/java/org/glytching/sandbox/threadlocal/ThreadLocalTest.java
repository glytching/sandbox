package org.glytching.sandbox.threadlocal;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLocalTest {
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalTest.class);

    @Test
    public void test() {
        ThreadLocalSUT mockedRequest = new ThreadLocalSUT();
        ThreadLocalSUT.REQUEST_HOLDER.set(mockedRequest);

        ThreadLocalSUT.doSomeMethodHere();

        Assert.assertNotNull(ThreadLocalSUT.REQUEST_HOLDER.get());
        logger.info("Current thread id: {}", Thread.currentThread().getId());
    }

}

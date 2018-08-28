package org.glytching.sandbox.rx;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.glytching.sandbox.rx.RxMapWrapper.wrapAndThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class RxWrapAndThrowTest {
    private static final Logger logger = LoggerFactory.getLogger(RxWrapAndThrowTest.class);

    @Test
    public void canWrapAndThrow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        List<String> received = Lists.newArrayList();
        List<Throwable> failures = Lists.newArrayList();

        Observable<String> observable = Observable.just("good", "bad")
                .map(wrapAndThrow(s -> {
                    if ("bad".equals(s)) {
                        throw new RuntimeException("boom!");
                    }
                    return s;
                }));

        logger.info("Subscribing");
        observable.subscribe(
                event -> {
                    received.add(event);
                    latch.countDown();
                },
                failures::add,
                () -> logger.info("Completed")
        );

        latch.await();

        assertThat(received.size(), is(1));
        assertThat(received, hasItem("good"));

        assertThat(failures.size(), is(1));
        assertThat(failures.get(0).getMessage(), is("boom!"));
    }
}
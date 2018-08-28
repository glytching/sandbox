package org.glytching.sandbox.rx;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxRetryWhenTest {
    private static final Logger logger = LoggerFactory.getLogger(RxRetryWhenTest.class);

    @Test
    public void canRetryWhen() {
        final Observable<Object> observable = Observable.create(emitter -> {
            // Code ...
        });

        observable
                .subscribeOn(Schedulers.io())
                .retryWhen(observable1 -> {
                    observable1.flatMap((Func1<Throwable, Observable<?>>) Observable::error);
                    logger.info("Retrying ...");
                    return observable1.retry();
                })
                .subscribe(
                        next -> logger.info("In subscribe.next"),
                        error -> logger.info("In subscribe.error")
                );
    }
}
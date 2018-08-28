package org.glytching.sandbox.rx;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class RxTest {
    private static final Logger logger = LoggerFactory.getLogger(RxTest.class);

    @Test
    public void example() {
        List<String> words = Arrays.asList(
                "the",
                "quick",
                "brown",
                "fox",
                "jumped",
                "over",
                "the",
                "lazy",
                "dogs"
        );

        logger.info("Output words ...");
        // from converts an array or iterable to a series of events, one per element
        // the alternative, just(), simply passes on the list/array
        Observable.from(words)
                .subscribe(logger::info);

        logger.info("Output words with line numbers ...");
        Observable.from(words)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string))
                .subscribe(logger::info);

        logger.info("Output letters ...");
        Observable.from(words)
                .flatMap(word -> Observable.from(word.split("")))
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string))
                .subscribe(logger::info);

        logger.info("Output distinct letters ...");
        Observable.from(words)
                .flatMap(word -> Observable.from(word.split("")))
                .distinct()
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string))
                .subscribe(logger::info);
    }
}
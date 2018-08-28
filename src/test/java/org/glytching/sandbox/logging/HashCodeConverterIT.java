package org.glytching.sandbox.logging;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * An integration test for {@link HashCodeConverter} which verifies the usage in
 * a real-world logback configuration.
 * <p>
 * This currently uses {@code logback-test.xml} in the test resources directory
 * which is not very elegant and might cause trouble if that file is used for
 * the test logger configuration as it's intended.
 *
 * @author richter
 */
public class HashCodeConverterIT {

    /**
     * Test of convert method, of class HashCodeConverter.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testConvert() throws IOException {
        Logger logger = LoggerFactory.getLogger(HashCodeConverterIT.class);
        logger.info("message");
        File logFile = new File("hash-code-converter-it.log");
        String logMessage = IOUtils.toString(new FileInputStream(logFile),
                Charsets.UTF_8).trim();
        Pattern pattern =
                Pattern.compile("\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d \\[main\\] INFO  o.g.s.logging.HashCodeConverterIT \\[(?<id>[0-9]+)\\] - message");
        Matcher matcher = pattern.matcher(logMessage);
        matcher.matches();
        String firstHashCode = matcher.group("id");
        int iterationCount = 10;
        for (int i = 0; i < iterationCount; i++) {
            logger.info("message");
        }
        logMessage = IOUtils.toString(new FileInputStream(logFile),
                Charsets.UTF_8);
        String[] logMessageLines = logMessage.split("\n");
        for (String logMessageLine : logMessageLines) {
            assertThat(String.format("message '%s' doesn't match regex with firstHashCode %s",
                    logMessageLine,
                    firstHashCode),
                    logMessageLine.matches(String.format("\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d \\[main\\] INFO  o.g.s.logging.HashCodeConverterIT \\[%s\\] - message",
                            firstHashCode)), is(true));
        }
        //the complete content of logFile could be evaluated with one regex
        //match using back references
    }
}

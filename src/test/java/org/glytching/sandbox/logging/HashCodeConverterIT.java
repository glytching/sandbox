package org.glytching.sandbox.logging;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An integration test for {@link HashCodeConverter} which verifies the usage in
 * a real-world logback configuration.
 *
 * This currently uses {@code logback-test.xml} in the test resources directory
 * which is not very elegant and might cause trouble if that file is used for
 * the test logger configuration as it's intended.
 *
 * @author richter
 */
public class HashCodeConverterIT {

    /**
     * Test of convert method, of class HashCodeConverter.
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testConvert() throws FileNotFoundException,
            IOException {
        Logger logger = LoggerFactory.getLogger(HashCodeConverterIT.class);
        logger.info("message");
        File logFile = new File("hash-code-converter-it.log");
        String logMessage = IOUtils.toString(new FileInputStream(logFile),
                Charsets.UTF_8).trim();
        Pattern pattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d \\[main\\] INFO  o.g.s.logging.HashCodeConverterIT \\[(?<id>[0-9]+)\\] - message");
        Matcher matcher = pattern.matcher(logMessage);
        matcher.matches();
        String firstHashCode = matcher.group("id");
        int iterationCount = 10;
        for(int i=0; i<iterationCount; i++) {
            logger.info("message");
        }
        logMessage = IOUtils.toString(new FileInputStream(logFile),
                Charsets.UTF_8);
        String[] logMessageLines = logMessage.split("\n");
        for(String logMessageLine : logMessageLines) {
            assertTrue(String.format("message '%s' doesn't match regex with firstHashCode %s",
                            logMessageLine,
                            firstHashCode),
                    logMessageLine.matches(String.format("\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d \\[main\\] INFO  o.g.s.logging.HashCodeConverterIT \\[%s\\] - message",
                            firstHashCode)));
        }
        //the complete content of logFile could be evaluated with one regex
        //match using back references
    }
}

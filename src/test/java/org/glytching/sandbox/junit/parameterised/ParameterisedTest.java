package org.glytching.sandbox.junit.parameterised;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterisedTest {
    private static final Logger logger = LoggerFactory.getLogger(ParameterisedTest.class);

    private Integer inputNumber;
    private Boolean expectedResult;
    private ParameterisedSUT primeNumberChecker;

    @Before
    public void initialize() {
        primeNumberChecker = new ParameterisedSUT();
    }

    // Each parameter should be placed as an argument here
    // Every time runner triggers, it will pass the arguments
    // from parameters we defined in primeNumbers() method

    public ParameterisedTest(Integer inputNumber, Boolean expectedResult) {
        this.inputNumber = inputNumber;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {2, true},
                {6, false},
                {19, true},
                {22, false},
                {23, true}
        });
    }

    // This test will run 4 times since we have 5 parameters defined
    @Test
    public void testPrimeNumberChecker() {
        logger.info("Parameterised Number is: {}", inputNumber);
        assertEquals(expectedResult, primeNumberChecker.validate(inputNumber));
    }
}
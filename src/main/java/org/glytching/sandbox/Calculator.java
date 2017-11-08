package org.glytching.sandbox;

/**
 * A noddy class to facilitate experimenting with Travis CI integration for:
 * <ul>
 * <li>Generating and publishing Javadocs</li>
 * </ul>
 */
public class Calculator {

    /**
     * Adds two values to each other.
     *
     * @param a an integer to be added to the value of {@code b}
     * @param b an integer to be added to the value of {@code a}
     *
     * @return the sum of {@code a} and {@code b}
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Subtracts one value from another.
     *
     * @param a an integer from which the value of {@code b} is to be subtracted
     * @param b an integer to be subtracted from the value of {@code a}
     * @return {@code b} subtracted from {@code a}
     */
    public int subtract(int a, int b) {
        return a - b;
    }
}
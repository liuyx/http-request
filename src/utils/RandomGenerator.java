/**
 * (c) 2012 PPLive Inc.
 * 
 * @author: Danny Sun <dannysun@pplive.com>
 * @date: 2012-03-05
 */
package utils;

import java.util.Random;

/* Class: RandomGenerator */
/**
 * This class is an extension to the <code>Random</code> class in
 * <code>java.util</code> that has the following advantages over the original:
 * <p>
 * <ul>
 * <li>The name of the class emphasizes that the object is a random
 * <u>generator</u> rather than a random value.
 * <p>
 * <li>The class includes overloaded versions of <code>nextInt</code> and
 * <code>nextDouble</code> to simplify choosing numbers in a specific range.
 * <p>
 * <li>The method <code>nextBoolean</code> is overloaded to allow the
 * specification of a probability.
 * <li>The class includes a method <code>nextColor</code> that generates a
 * random opaque color.
 * <p>
 * </ul>
 * <p>
 */

public class RandomGenerator extends Random
{

    private static final long serialVersionUID = 1L;

    /* Method: nextInt(n) */
    /**
     * Returns the next random integer between 0 and <code>n</code>-1,
     * inclusive. This method is in modern implementations of the
     * <code>Random</code> class, but is missing from JDK 1.1.
     * 
     * @usage int k = rgen.nextInt(n);
     * @param n The size of the range
     * @return The next random <code>int</code> between <code>0</code> and
     *         <code>n</code>-1, inclusive
     */

    public int nextInt(int n)
    {
        return nextInt(0, n - 1);
    }

    /* Method: nextInt(low, high) */
    /**
     * Returns the next random integer in the specified range.
     * 
     * @usage int k = rgen.nextInt(low, high)
     * @param low The low end of the range
     * @param high The high end of the range
     * @return The next random <code>int</code> between <code>low</code> and
     *         <code>high</code>, inclusive
     */
    public int nextInt(int low, int high)
    {
        return low + (int) ((high - low + 1) * nextDouble());
    }

    /* Method: nextDouble(low, high) */
    /**
     * Returns the next random real number in the specified range.
     * 
     * @usage double d = rgen.nextDouble(low, high)
     * @param low The low end of the range
     * @param high The high end of the range
     * @return The next random <code>double</code> in the half-open interval [
     *         low : high )
     */
    public double nextDouble(double low, double high)
    {
        return low + (high - low) * nextDouble();
    }

    /* Method: nextBoolean() */
    /**
     * Returns a random <code>boolean</code> value that is <code>true</code>
     * or <code>false</code> with equal probability. This method is in modern
     * implementations of the <code>Random</code> class, but is missing from
     * JDK 1.1.
     * 
     * @usage if (rgen.nextBoolean()) . . .
     * @return The value <code>true</code> with probability 0.5
     */
    public boolean nextBoolean()
    {
        return nextBoolean(0.5);
    }

    /* Method: nextBoolean(probability) */
    /**
     * Returns a random <code>boolean</code> value with the specified
     * probability.
     * 
     * @usage if (rgen.nextBoolean(p)) . . .
     * @param p A value between 0 (impossible) and 1 (certain) indicating the
     *            probability
     * @return The value <code>true</code> with probability <code>p</code>
     */
    public boolean nextBoolean(double p)
    {
        return nextDouble() < p;
    }

    // /* Method: nextColor() */
    // /**
    // * Returns a random opaque <code>Color</code> whose components are
    // chosen
    // * uniformly in the 0-255 range.
    // *
    // * @usage Color color = rgen.newColor()
    // * @return A random opaque <code>Color</code>
    // */
    // public Color nextColor()
    // {
    // return new Color(nextInt(256), nextInt(256), nextInt(256));
    // }
}

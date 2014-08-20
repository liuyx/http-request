package utils;

/**
 * Created with IntelliJ IDEA.
 * User: youxueliu
 * Date: 13-7-11
 * Time: 下午6:33
 */
public final class PreConditions
{
    private PreConditions(){}


    public static <T> T checkNotNull(T reference)
    {
        if(reference == null)
            throw new NullPointerException();
        return reference;
    }

    public static <T> T checkNotNull(T reference,Object msg)
    {
        if(reference == null)
            throw new NullPointerException(String.valueOf(msg));
        return reference;
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     * @param expression a boolean expression
     * @throws IllegalArgumentException if expression is false
     */
    public static void checkArgument(boolean expression)
    {
        if(!expression)
            throw new IllegalArgumentException();
    }

    /**
     * Ensures the truth of an expression involving one or more parameters to the calling method.
     * @param expression a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a string using String.valueOf(Object)
     * @throws IllegalArgumentException if expression is false
     */
    public static void checkArgument(boolean expression,Object errorMessage)
    {
        if(!expression)
            throw new IllegalArgumentException(String.valueOf(errorMessage));
    }

    /**
     *Ensures the truth of an expression involving the state of the calling instance, but not involving any parameters to the calling method.
     * @param expression a boolean expression
     * Throws:
     *      IllegalStateException if expression is false
     */
    public static void checkState(boolean expression)
    {
        if(!expression)
            throw new IllegalStateException();
    }

    /**
     * Ensures the truth of an expression involving the state of the calling instance, but not involving any parameters to the calling method.
     * @param expression a boolean expression
     * @param errorMessage the exception message to use if the check fails; will be converted to a string using String.valueOf(Object)
     * @throws IllegalStateException if expression is false
     */
    public static void checkState(boolean expression,Object errorMessage)
    {
        if(!expression)
            throw new IllegalStateException(String.valueOf(errorMessage));
    }
}

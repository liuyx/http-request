package utils;

/**
 * User: youxueliu
 * Date: 13-9-25
 * Time: 上午11:21
 * 某种准则
 */
public interface Rule<T>
{
    /**
     * 筛选准则，通过该准则，则返回true，否则，返回false
     * @param t
     * @return
     */
    boolean rule(T t);
}

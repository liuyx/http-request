package utils;

/**
 * User: youxueliu
 * Date: 13-9-25
 * Time: 上午10:59
 * 经过Function接口的handle处理之后，返回新的T对象
 */
public interface Function<T>
{
    /**
     * 将参数t传入该方法之后，经处理，将结果返回
     * @param t
     * @return
     */
    T handle(T t);
}

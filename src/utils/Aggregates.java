package utils;

import java.util.Collection;

/**
 * User: youxueliu
 * Date: 13-9-6
 * Time: 下午3:05
 * 容器的工具方法
 */
public class Aggregates {
    /**
     * 使用joinStr将每个元素连接起来，并将连接的结果返回
     * @param src 待处理的列表
     * @param joinStr 连接的字符串
     * @param <T>
     * @return
     */
    public static <T> String join(Collection<? extends T> src,String joinStr)
    {
        if(src == null || src.size() == 0 || Strings.isNullOrEmpty(joinStr)) return "";
        StringBuilder sb = new StringBuilder();
        for(T t: src)
        {
            if(t == null)continue;
            sb.append(t.toString()).append(joinStr);
        }
        sb.delete(sb.length()-joinStr.length(),sb.length());
        return sb.toString();
    }

    /**
     * 将一个容器(src)的元素减去另一个容器中(dest)的元素，将结果填充到result容器中
     * @param src
     * @param dest
     * @param <T>
     * @return
     */
    public static <T> void substract(Collection<? extends T> src,Collection<? extends T> dest,Collection<? super T> result)
    {
        if(src == null || dest == null)return;
        for(T t: src)
            if(!dest.contains(t))
                result.add(t);
    }

    /**
     * 将n个相同的值填充到容器的末尾
     * @param collection
     * @param t
     * @param nTimes
     * @param <T>
     */
    public static <T> void nFill(Collection<? super T> collection,T t,int nTimes)
    {
        if(collection == null)return;
        if(nTimes > 0)
            for(int i = 0; i < nTimes; i++)
                collection.add(t);
    }

    public static boolean isNullOrEmpty(Collection<?> collection)
    {
        return collection == null || collection.isEmpty();
    }

    /**
     * 参数中，只要有任何一个容器为null或者空时都返回true
     * @param collections
     * @return
     */
    public static boolean isAnyNullOrEmpty(Collection<?>...collections)
    {
        if(collections == null || collections.length == 0)return true;
        for(Collection<?> c: collections)
            if(isNullOrEmpty(c))
                return true;
        return false;
    }

    /**
     * 参数中，所有容器为null或者空时都返回true
     * @param collections
     * @return
     */
    public static boolean isAllNullOrEmpty(Collection<?>...collections)
    {
        if(collections == null || collections.length == 0)return true;
        for(Collection<?> c: collections)
            if(!isNullOrEmpty(c)) return false;
        return true;
    }

    /**
     * 遍历参数，假如某个参数容器含有元素，将该容器清空
     * @param collections
     */
    public static void allClearDataIfHasElement(Collection<?>...collections)
    {
        if(collections == null || collections.length == 0)return;
        for(Collection<?> c: collections)
            if(!isNullOrEmpty(c))
                c.clear();
    }
}

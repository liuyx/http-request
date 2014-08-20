package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: youxueliu
 * Date: 13-7-12
 * Time: 下午2:07
 */
public final class Lists
{

    private Lists(){}
    /**
     * 类型迭代的简便方法
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T>  newArrayList()
    {
        return new ArrayList<T>();
    }

    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList()
    {
        return new CopyOnWriteArrayList<T>();
    }

    /**
     * 当list不为空时，返回其容量，否则，返回0
     * @param list
     * @return
     */
    public static int getSize(List<?> list)
    {
        return list != null ? list.size() : 0;
    }

    /**
     * 将list的第index个位置的值flip一下，原先为true的变成false，false的变成true
     * @param index
     * @param list
     */
    public static void flipBoolAtIndex(List<Boolean> list,int index)
    {
         if(list == null || list.size() <= index)return;
         final boolean state = list.get(index);
         list.set(index,!state);
    }

    /**
     * 将容器中的每个元素都经过{@link Function#handle(Object)}的处理，得到容器的新元素
     * @param list
     * @param f
     * @param <T>
     */
    public static <T> void forEach(List<T> list,Function<T> f)
    {
        if(list == null)return;
        int i = 0;
        for(T t: list)
            list.set(i++,f.handle(t));
    }

    /**
     * 将容器中的每个元素都经过{@link Function#handle(Object)}的处理，得到一个新容器
     * @param list
     * @param f
     * @param <T>
     * @return
     */
    public static <T> List<T> forEachBackup(List<T> list,Function<T> f)
    {
        if(list == null)return list;
        List<T> result = newArrayList();
        for(T t: list)
            result.add(f.handle(t));
        return result;
    }

    /**
     * 将容器的元素经过某一个准则函数筛选，元素经过准则函数处理后返回true的，添加到返回的结果list中
     * @param list
     * @param r
     * @param <T>
     * @return
     */
    public static <T> List<T> select(List<T> list,Rule<T> r)
    {
        if(list == null)return list;
        List<T> result = newArrayList();
        for(T t: list)
            if(r.rule(t))
                result.add(t);
        return result;
    }

    /**
     * 将一个容器的元素减去另一个容器中的元素，将结果返回
     * @param src
     * @param dest
     * @param <T>
     * @return
     */
    public static <T> List<T> substract(List<T> src,List<T> dest)
    {
        if(src == null || dest == null)return src;
        List<T> result = new ArrayList<T>();
        for(T t: src)
            if(!dest.contains(t))
                result.add(t);
        return result;
    }

    public static <T> void add(List<? super T> dest,List<? extends T> src)
    {
        if (Aggregates.isNullOrEmpty(src) || dest == null) return;
        for (T t : src)
            dest.add(t);
    }

    /**
     * 将数组的元素填充到ArrayList中，并返回ArrayList
     * @param array
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> toArrayList(T[] array)
    {
        if(array == null)return null;
        ArrayList<T> result = newArrayList();
        for(T t: array)
            result.add(t);
        return result;
    }

    /**
     * 返回一个新的ArrayList
     * @param arrays 可变参数
     * @param <T> 某种类型
     * @return 返回一个新的ArrayList
     */
    @SafeVarargs
	public static <T> ArrayList<T> newArrayList(T... arrays)
    {
        return toArrayList(arrays);
    }

}

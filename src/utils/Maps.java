package utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: youxueliu
 * Date: 13-7-12
 * Time: 下午2:08
 */
public class Maps
{
    public static <K,V> HashMap<K,V>  newHashMap()
    {
        return new HashMap<K, V>();
    }

    public static <K,V> LinkedHashMap<K,V> newLinkedHashMap()
    {
        return new LinkedHashMap<K,V>();
    }

    public static <K,V> TreeMap<K,V> newTreeMap()
    {
        return new TreeMap<K, V>();
    }

    public static <K,V>ConcurrentHashMap<K,V> newConcurrentHashMap()
    {
        return new ConcurrentHashMap<K, V>();
    }

    /**
     * 将两个Map中的参数合并，合并结果放在第一个参数中
     * @param mergeResult
     * @param src
     * @param <K>
     * @param <V>
     */
    public static <K,V> void union(Map<? super K,? super V> mergeResult,Map<? extends K,? extends V> src)
    {
        PreConditions.checkNotNull(mergeResult);
        PreConditions.checkNotNull(src);
        for(Map.Entry<? extends K,? extends V> entry: src.entrySet())
            mergeResult.put(entry.getKey(),entry.getValue());
    }

    /**
     * 将几个Map中的参数合并，合并结果放在第一个参数中
     * @param mergeResult
     * @param srcList
     * @param <K>
     * @param <V>
     */
    @SafeVarargs
	public static <K,V> void union(Map<? super K,? super V> mergeResult,Map<? extends K,? extends V>...srcList)
    {
        PreConditions.checkNotNull(mergeResult);
        PreConditions.checkNotNull(srcList);
        for(Map<? extends K,? extends V> map: srcList)
            if(map != null)
                union(mergeResult,map);
    }

    /**
     * 将一个map的元素拷贝到hashmap中返回
     * @param src
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> HashMap<K,V> copy(Map<K,V> src)
    {
        PreConditions.checkNotNull(src);
        HashMap<K,V> result = newHashMap();
        for(Map.Entry<K,V> entry: src.entrySet())
            result.put(entry.getKey(),entry.getValue());
        return result;
    }
}

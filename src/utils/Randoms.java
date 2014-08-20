package utils;

import java.util.Random;

/**
 * User: youxueliu
 * Date: 13-11-21
 * Time: 下午4:54
 */
public class Randoms
{
    private static Random sRandom;

    /**
     * 在基准值旁边随机偏移delta内的数
     * @param base
     * @param delta
     * @return
     */
    public static int getRandom(int base,int delta)
    {
        if (sRandom == null) sRandom = new Random();
        sRandom.setSeed(System.currentTimeMillis());
        final boolean add = sRandom.nextBoolean();
        if (add)
            base += sRandom.nextInt(delta);
        else
            base -= sRandom.nextInt(delta);
        return base;
    }
}

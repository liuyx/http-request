package utils;


/**
 * User: youxueliu Date: 13-8-20 Time: 下午5:47 替换大括号中的字符
 */
public class ReplaceBraceValue
{
    /**
     * 将url中大括号的字符替换成replaceStr
     * 
     * @param url
     * @param replaceStr
     * @return
     */
    public static String replace(String url, String replaceStr)
    {
        if (!Strings.isNullOrEmpty(url) && !Strings.isNullOrEmpty(replaceStr))
        {
            return url.replaceAll("[{].*?[}]", replaceStr);
        }
        return url;
    }

    /**
     * 将花括号中的字符依次替换为replaces数组中的字符串
     * 
     * @param src
     * @param replaces
     * @return
     */
    public static String replaces(String src, String... replaces)
    {
        if (!Strings.isNullOrEmpty(src) && replaces != null)
        {
            for (String replace : replaces)
            {
                if (!Strings.isNullOrEmpty(replace))
                {
                    src = src.replaceFirst("[{].*?[}]", replace);
                }
            }
        }

        return src;
    }
}

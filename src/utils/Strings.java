package utils;

/**
 * Created with IntelliJ IDEA.
 * User: youxueliu
 * Date: 13-7-12
 * Time: 下午2:06
 */
public class Strings
{
    public static boolean isNullOrEmpty(String s)
    {
        return s == null || s.equals("");
    }

    public static boolean isAllNotEmpty(String...strings)
    {
        if (strings == null) return false;
        for (String s : strings)
            if (isNullOrEmpty(s))
                return false;
        return false;
    }

    public static String nullToEmpty(String s)
    {
        return s == null ? "" : s;
    }

    /**
     * 将字符串首字母大写
     * @param src
     * @return
     */
    public static String capitalize(String src)
    {
        if(src == null || src.equals("")) return src;
        char[] chars = src.toCharArray();
        final int len = chars.length;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len; i++)
        {
            if(i == 0)
            {
                char capChar = cap(chars[0]);
                sb.append(capChar);
            } else{
                sb.append(chars[i]);
            }
        }

        return sb.toString();
    }

    /**
     * 从一个字符串减去另一个字符串得到的子字符串
     * @param src
     * @param target
     * @return
     */
    public static String subStract(String src,String target){
        if(isNullOrEmpty(src) || isNullOrEmpty(target))return src;
        final int index = src.indexOf(target);
        if(index == -1)return src;
        final String startStr = src.substring(0,index);
        final int endIndex = index + target.length();
        final String endStr = src.substring(endIndex);
        return startStr+endStr;
    }

    /**
     * 判断str是否全部包含chars字符数组中任意的元素，假如包含，则返回true，否则false
     * @param str
     * @param chars
     * @return
     */
    public static boolean contains(String str,char[] chars)
    {
        if (Strings.isNullOrEmpty(str) || chars == null) return false;
        for (char c : chars)
            if (str.indexOf(c) != -1)
                return false;
        return true;
    }

    public static boolean contains(String[] src,String target)
    {
        if (src == null || isNullOrEmpty(target)) return false;
        for (String s: src)
            if (!isNullOrEmpty(target) && !isNullOrEmpty(s) && s.equals(target))
                return true;
        return false;
    }

    /**
     * 从标准包名获得前缀
     * @param canonicalName
     * @return
     */
    public static String getPrefixPackName(String canonicalName){
        String[] strs = canonicalName.split("[.]");
        int len = strs.length;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len - 1; i++){
            sb.append(strs[i]).append(".");
        }
        sb.delete(sb.length()-1,sb.length());
        return sb.toString();
    }

    private static char cap(char src)
    {
        return (char)(src - 32);
    }

    public static String join(String src, String joinStr)
    {
        if(isNullOrEmpty(src) || isNullOrEmpty(joinStr)) return src;
        final StringBuilder sb = new StringBuilder();
        for(char c: src.toCharArray())
            sb.append(c).append(joinStr);
        sb.delete(sb.length()-joinStr.length(),sb.length());
        return sb.toString();
    }


    /**
     * 从url中获得baseUrl
     * @param url
     * @return
     */
    public static String getBaseUrlFrom(String url){
        if(url == null || url.equals(""))return url;
        final String[] array = url.split("\\?");
        if(array != null && array.length > 0) return array[0];
        return url;
    }

    /**
     * 从url中获得查询参数的url
     * @param url
     * @return
     */
    public static String getParamsStrFromUrl(String url){
        if(url == null || url.equals(""))return url;
        final String[] array = url.split("\\?");
        if(array != null && array.length > 1) return array[1];
        return url;
    }

    /**
     * 从路径名中获取文件名
     * @param path
     * @return
     */
    public static String getFileNameFromPath(String path)
    {
        if (Strings.isNullOrEmpty(path)) return path;
        return path.substring(path.lastIndexOf('/')+1);
    }
}

package http;

import java.util.Map;

import utils.LogUtils;
import utils.Maps;
import utils.PreConditions;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * User: youxueliu Date: 13-9-9 Time: 上午11:26 适合于PPTV网络请求的通用参数类
 * 由于Http的参数都可以用String表示，故该类的Bundle参数和值均为String值
 */
public class HttpParameters
{
    /**
     * pptv所有的http请求通用的参数
     * 
     * @param context
     * @return
     */
    public static Bundle getCommonParams(Context context)
    {
        PreConditions.checkNotNull(context);
        final Bundle result = new Bundle();
        result.putString("from", "aph");
        result.putString("format", "json");
        result.putString("version", getLocalVersionName(context));
        result.putString("appid", context.getPackageName());
        result.putString("appplt", "aph");
        return result;
    }

    /**
     * pptv所有的http请求通用的参数
     * 
     * @param context
     * @return
     */
    public static Map<String, String> getCommonParamsWithMap(Context context)
    {
        PreConditions.checkNotNull(context);
        return bundleToMap(getCommonParams(context));
    }

    /**
     * 添加通用参数（from=aph,format=json,version)等
     * 
     * @param context
     * @param dest
     */
    public static void addCommonParams(Context context, Bundle dest)
    {
        if (dest == null)
            return;
        final Bundle commonParams = getCommonParams(context);
        addParamsFromAnother(commonParams, dest);
    }

    /**
     * 将一个Bundle的参数加到另一个Bundle里
     * 
     * @param src
     * @param dest
     */
    public static void addParamsFromAnother(Bundle src, Bundle dest)
    {
        if (src == null || dest == null)
            return;
        for (String key : src.keySet())
            dest.putString(key, src.getString(key));
    }

    public static void addParamsFrom(Map<String, String> src, Map<String, String> dest)
    {
        if (src == null || dest == null)
            return;
        for (String key : src.keySet())
            dest.put(key, src.get(key));
    }

    /**
     * 全是key-value的参数转换为map
     * 
     * @param params
     * @return
     */
    public static Map<String, String> bundleToMap(Bundle params)
    {
        if (params == null || params.isEmpty())
            return null;
        Map<String, String> result = Maps.newHashMap();
        for (String key : params.keySet())
            result.put(key, params.getString(key));
        return result;
    }

    /**
     * 全是key-value的Bundle参数转换为用&连接的String
     * 
     * @param data
     * @return
     */
    public static String bundleParamsToStr(Bundle data)
    {
        return mapToStr(bundleToMap(data));
    }

    /**
     * 将键值都是String的map对象转换为用&连接的字符串
     * 
     * @param params
     * @return
     */
    public static String mapToStr(Map<String, String> params)
    {
        if (params == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            sb.append(entry.getKey() + "=" + entry.getValue()).append("&");
        }
        sb.delete(sb.length() - 1, sb.length());

        return sb.toString();
    }

    /**
     * 产生这样的字符串：http:xxx/xxx?xxx=xxx&xxx=xxx
     * 
     * @param baseUrl
     * @param params
     * @return
     */
    public static String genGetUrl(String baseUrl, Map<String, String> params)
    {
        if (baseUrl == null)
            return null;
        StringBuilder sb = new StringBuilder(baseUrl + "?");
        sb.append(mapToStr(params));
        return sb.toString();
    }

    /**
     * 产生这样的字符串：http:xxx/xxx?xxx=xxx&xxx=xxx
     * 
     * @param baseUrl
     * @param params 键值都是String
     * @return
     */
    public static String genGetUrl(String baseUrl, Bundle params)
    {
        return genGetUrl(baseUrl, bundleToMap(params));
    }

    // -----------------------------------私有云通用参数---------------------------------

    /**
     * 私有云所有的http接口都穿了username和token，故可封装之
     */
    public static final class PriCloudComParams
    {

        /**
         * 封装了pptv http请求通用的三个参数（from，format，version）
         * 还添加了私有云共有的另外两个参数(username, token)
         * 
         * @param context
         * @return
         */
        public static Map<String, String> getComParams(Context context)
        {
            PreConditions.checkNotNull(context);
            final Map<String, String> pptvComParams = getCommonParamsWithMap(context);
            final Map<String, String> result = pptvComParams;
//            try
//            {
//                result.put("username", URLEncoder.encode(AccountPreferences.getUsername(context), "UTF-8"));
//            }
//            catch (Exception e)
//            { /* ignore */
//            }
//            String token = AccountPreferences.getLoginToken(context);
//
//            LogUtils.debug("liuyx token = " + token);
//            result.put("token", token);

//            result.put("from", UUIDDatabaseHelper.getInstance(context).getUUID());

            return result;
        }
    }

    /** 本地版本号 */
    public static String getLocalVersionName(Context context)
    {
        if (context == null)
        {
            return "";
        }
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            LogUtils.error(e.toString(), e);
        }
        return "";
    }

}

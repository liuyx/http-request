package config;

import java.util.Map;

import utils.Maps;

/**
 * User: youxueliu
 * Date: 13-11-18
 * Time: 下午3:45
 * 云播环境变量配置类
 */
public class CloudEnv
{
    public static final String TAG = "task_upload";

    /**
     * 任务是否取消状态Map
     */
    private static final Map<String,Boolean> TASK_STATE_MAP = Maps.newConcurrentHashMap();                    // 监控任务是否取消的Map
    private static final Map<String,Long> TASK_UPLOAD_SIZE_MAP = Maps.newConcurrentHashMap();
    private static final Map<String,Boolean> TASK_RUN_STATE_MAP = Maps.newConcurrentHashMap();                // 监控任务是否运行的Map

    private static final Map<String,Long> SEG_UPLOAD_MAP = Maps.newConcurrentHashMap();

    public static void setCancelState(String key,boolean state)
    {
        TASK_STATE_MAP.put(key,state);
    }

    public static Boolean isCancel(String key)
    {
        return TASK_STATE_MAP.get(key);
    }

    public static Boolean isStart(String key)
    {
        final Boolean isStart = TASK_RUN_STATE_MAP.get(key);
        return isStart == null ? false : isStart;
    }

    public static void setTaskStartState(String key)
    {
        TASK_RUN_STATE_MAP.put(key,true);
    }

    public static void setTaskStopState(String key)
    {
        TASK_RUN_STATE_MAP.put(key,false);
    }

    public static void setUploadSize(String key,long uploadSize)
    {
        TASK_UPLOAD_SIZE_MAP.put(key,uploadSize);
    }

    public static void putStartTime(String key,long time)
    {
        SEG_UPLOAD_MAP.put(key,time);
    }

    public static Long getTime(String key)
    {
        return SEG_UPLOAD_MAP.get(key) == null ? 0 : SEG_UPLOAD_MAP.get(key);
    }

    public static enum Err
    {
        /**
         * 创建索引出错
         */
        ECREAT,

        /**
         * 私有云上传时出错
         */
        EUPLOAD,

        /**
         * 调进度接口出错
         */
        EPROGRESS,

        /**
         * 计算MD5时出错
         */
        ECALCMD5,

        /**
         * 上传MD5时出错
         */
        EUPLOADMD5,

        /**
         * 计算特征值时出错
         */
        ECALCFEATURES,

        /**
         * 上传特征值时出错
         */
        EUPLOADFEATURES,

        /**
         * 请求range时出错
         */
        ERANGE,

        /**
         * 上传range时出错
         */
        EUPLOADRANGE
    }

}

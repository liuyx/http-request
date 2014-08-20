package utils;

import java.text.SimpleDateFormat;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author sugarzhang
 */
public class LogUtils
{
    /**
     * 日志级别
     */
//    public static int LOG_LEVEL = Log.ERROR + 1;
    public static int LOG_LEVEL = 1;
    /**
     * 异常栈位移
     */
    private static final int EXCEPTION_STACK_INDEX = 2;

    // private static final File DIR = new
    // File(Environment.getExternalStorageDirectory() + "/pptv/log/");

    // private static final String FILE_NAME = "log.txt";

    @SuppressWarnings("unused")
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // private static synchronized void save2file(String level, String tag,
    // String msg)
    // {
    //
    // if
    // (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
    // {
    // if (!DIR.exists())
    // {
    // if (!DIR.mkdirs())
    // {
    // return;
    // }
    // }
    //
    // FileOutputStream outputStream = null;
    // File file = new File(DIR, FILE_NAME);
    // if (file.exists() && file.length() > 1024 * 100)
    // {
    // file.delete();
    // }
    // try
    // {
    // outputStream = new FileOutputStream(file, true);
    // outputStream.write(("[" + FORMAT.format(new Date()) + "] [" + level +
    // "] [" + tag + "] " + msg + "\n").getBytes());
    // }
    // catch (FileNotFoundException e)
    // {
    //
    // }
    // catch (IOException e)
    // {
    //
    // }
    // finally
    // {
    // if (outputStream != null)
    // {
    // try
    // {
    // outputStream.close();
    // }
    // catch (IOException e)
    // {
    //
    // }
    // }
    // }
    // }
    // }

    /**
     * verbose级别的日志
     * 
     * @param msg 打印内容
     * @see [类、类#方法、类#成员]
     */
    public static void verbose(String msg)
    {
        if (Log.VERBOSE >= LOG_LEVEL)
        {
            Log.v(getTag(), msg);
        }
    }

    /**
     * debug级别的日志
     * 
     * @param msg 打印内容
     * @see [类、类#方法、类#成员]
     */
    public static void debug(String msg)
    {
        if (Log.DEBUG >= LOG_LEVEL)
        {
            Log.d(getTag(), msg);
        }
    }

    /**
     * info级别的日志
     * 
     * @param msg 打印内容
     * @see [类、类#方法、类#成员]
     */
    public static void info(String msg)
    {
        if (Log.INFO >= LOG_LEVEL)
        {
            Log.i(getTag(), msg);
        }
    }

    /**
     * warn级别的日志
     * 
     * @param msg 打印内容
     * @see [类、类#方法、类#成员]
     */
    public static void warn(String msg)
    {
        if (Log.WARN >= LOG_LEVEL)
        {
            Log.w(getTag(), msg);
        }
    }

    /**
     * error级别的日志
     * 
     * @param msg 打印内容
     * @see [类、类#方法、类#成员]
     */
    public static void error(String msg)
    {
        if (Log.ERROR >= LOG_LEVEL)
        {
            String tag = getTag();
            Log.e(tag, msg);
        }
        ApplogManager.getInstance().invokeMethod(ApplogManager.ACTION_LOGCAT, msg);
    }

    public static void error(String msg, Throwable tr)
    {
        if (Log.ERROR >= LOG_LEVEL)
        {
            String tag = getTag();
            Log.e(tag, msg, tr);
        }
        ApplogManager.getInstance().invokeMethod(ApplogManager.ACTION_LOGCAT, msg + (", throws: " + tr));
    }

    // public static void error(String msg, boolean save)
    // {
    // if (Log.ERROR >= LOG_LEVEL)
    // {
    // String tag = getTag();
    // if (save)
    // {
    // save2file("e", tag, msg);
    // }
    //
    // Log.e(tag, msg);
    // }
    // }
    //
    // public static void error(Throwable tr, boolean save)
    // {
    //
    // if (Log.ERROR >= LOG_LEVEL)
    // {
    // String tag = getTag();
    // String msg = Log.getStackTraceString(tr);
    // if (save)
    // {
    // save2file("e", tag, msg);
    // }
    //
    // Log.e(tag, msg);
    // }
    // }

    /**
     * 获取日志的标签 格式：类名_方法名_行号 （需要权限：android.permission.GET_TASKS）
     * 
     * @return tag
     * @see [类、类#方法、类#成员]
     */
    private static String getTag()
    {
        try
        {
            Exception exception = new LogException();
            if (exception.getStackTrace() == null || exception.getStackTrace().length <= EXCEPTION_STACK_INDEX)
            {
                return "***";
            }
            StackTraceElement element = exception.getStackTrace()[EXCEPTION_STACK_INDEX];

            String className = element.getClassName();

            int index = className.lastIndexOf(".");
            if (index > 0)
            {
                className = className.substring(index + 1);
            }

            return className + "_" + element.getMethodName() + "_" + element.getLineNumber();

        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return "***";
        }
    }

    /**
     * 取日志标签用的的异常类，只是用于取得日志标签
     */
    private static class LogException extends Exception
    {
        /**
         * 注释内容
         */
        private static final long serialVersionUID = 1L;
    }
}

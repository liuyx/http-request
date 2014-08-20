package utils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * APP日志管理，网络请求错误的日志缓存到本地，用于故障诊断
 */
public class ApplogManager
{
    public static final int ACTION_APPSTART = 1;

    public static final int ACTION_APP_START_OK = 2;

    public static final int ACTION_LOGIN = 3;

    public static final int ACTION_LOGOUT = 4;

    public static final int ACTION_HTTP_REQUEST = 5;

    public static final int ACTION_DOWNLOAD_ERROR = 6;

    public static final int ACTION_PLAYER = 7;

    public static final int ACTION_LOGCAT = 8;
    
    public static final int ACTION_CRASH = 9;

    public static final String TAG_BASE = "[BASE_INFO]";

    public static final String TAG_START = "[START_INFO]";

    public static final String TAG_PASSPORT = "[PASSPORT_INFO]";

    public static final String TAG_PLAY = "[PLAY_INFO]";

    public static final String TAG_DOWNLOAD = "[DOWNLOAD_INFO]";

    public static final String TAG_HTTP = "[HTTP_INFO]";

    public static final String TAG_LOGCAT = "[LOGCAT_ERROR]";
    
    public static final String TAG_CRASH = "[LOGCAT_CRASH]";

    // /**
    // * 启动开关，如果为false则日志不做记录，目前只有APHONE版本使用
    // */
    // public static boolean openLog;

    private static ApplogManager instance;

    private ApplogInfo applogInfo;

    private Context context;

    private File cacheLog;

    /**
     * 当cacheLog超过100K时候保存到这个文件，如果存在则删除之前的。
     */
    private File cacheLogSaved;

    private FileOutputStream fos;

    private BufferedWriter bw;

    private LinkedList<String> listDataToWrite;

    private WriteFileThread thread;

    private boolean inited;

    public static ApplogManager getInstance()
    {
        if (instance == null)
        {
            synchronized (ApplogManager.class)
            {
                if (instance == null)
                {
                    instance = new ApplogManager();
                }
            }
        }
        return instance;
    }

    // public static void release()
    // {
    // if (instance != null)
    // {
    // final ApplogManager in = instance;
    // in.setInited(false);
    //
    // if (in.listDataToWrite != null)
    // {
    // synchronized (in.listDataToWrite)
    // {
    // in.listDataToWrite.clear();
    // in.listDataToWrite.notifyAll();
    // }
    // }
    //
    // synchronized (ApplogManager.class)
    // {
    // instance = null;
    // }
    // }
    //
    // }

    /**
     * 将缓存中的applog写入文件
     */
    public void writeCacheApplog(OutputStream os)
    {
        if (!inited)
        {
            return;
        }

        FileInputStream fis = null;
        try
        {
            File[] files = {cacheLogSaved, cacheLog};
            for (File f : files)
            {
                if (!f.exists())
                {
                    continue;
                }
                fis = new FileInputStream(f);
                byte[] buffer = new byte[256];
                while (true)
                {
                    int i = fis.read(buffer);
                    if (i <= 0)
                    {
                        break;
                    }
                    os.write(buffer, 0, i);
                }
                os.flush();
            }
        }
        catch (Exception e)
        {
            // ingore;
        }
        finally
        {
            closeStream(fis);
        }
    }

    /**
     * 日志文件先写入基础信息 再写入缓存的日志信息 {@link #writeCacheApplog(OutputStream)};
     */
    public void writeBaseInfo(BufferedWriter os) throws IOException
    {
        if (!inited)
        {
            return;
        }
        if (os != null)
        {
            os.write(getTimeTag() + TAG_BASE + applogInfo.phoneName);
            os.newLine();
            os.write(getTimeTag() + TAG_BASE + applogInfo.phoneVerison);
            os.newLine();
            os.write(getTimeTag() + TAG_BASE + applogInfo.appVersionCode);
            os.newLine();
            os.write(getTimeTag() + TAG_BASE + applogInfo.appVersionName);
            os.newLine();
            os.write(getTimeTag() + TAG_BASE + applogInfo.uuid);
            os.newLine();
            os.write(getTimeTag() + TAG_BASE + applogInfo.imei);
            os.newLine();
            os.write(getTimeTag() + TAG_BASE + "IP:" + NetworkUtils.getIPAddress(true));
            os.newLine();
            os.write(getTimeTag() + TAG_BASE + applogInfo.mac);
            os.newLine();
            @SuppressWarnings("deprecation")
			String startTime = new Date(applogInfo.startTime).toLocaleString();
            @SuppressWarnings("deprecation")
			String startEndTime = new Date(applogInfo.startEndTime).toLocaleString();
            String costTime = ((applogInfo.startEndTime - applogInfo.startTime) / 1000) + " seconds";
            os.write(getTimeTag()
                    + TAG_BASE
                    + String.format("start time:%s, start end:%s, start cost:%s, start type:%s", startTime,
                            startEndTime, costTime, applogInfo.startType));
            os.newLine();

            // 屏幕

            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
            double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
            String screenStr = "Screen inches:" + screenInches + ",widthPixels:" + dm.widthPixels + ",heightPixels:"
                    + dm.heightPixels + ",density:" + dm.density;
            os.write(getTimeTag() + TAG_BASE + screenStr);

            // 个人信息
            writePassportInfo(os);
            os.newLine();
            os.flush();
        }
    }

    /**
     * 事件处理，需要记录到内存或者写入日志
     * 
     * @param action 类型
     * @param value 数据
     */
    public void invokeMethod(int action, String value)
    {
        if (!inited)
        {
            return;
        }
        switch (action)
        {
            case ACTION_APP_START_OK:
            {
                invokeAppStartEnd();
                break;
            }
            case ACTION_APPSTART:
            {
                invokeAppStart();
                applogInfo.startType = value;
                break;
            }
            case ACTION_HTTP_REQUEST:
            {
                invokeHttpRequest(value);
                break;
            }
            case ACTION_LOGIN:
            {
//                invokeLogin();
                break;
            }
            case ACTION_LOGOUT:
            {
                invokeLogout();
                break;
            }
            case ACTION_DOWNLOAD_ERROR:
            {
                invokeDownload(value);
                break;
            }
            case ACTION_PLAYER:
            {
                invokePlayer(value);
                break;
            }
            case ACTION_LOGCAT:
            {
                invokeLogcat(value);
                break;
            }
            case ACTION_CRASH:
            {
                invokeCrash(value);
                break;
            }

            default:
                break;
        }
    }

    // public static void release()
    // {
    // if (instance != null)
    // {
    // final ApplogManager in = instance;
    // in.setInited(false);
    //
    // if (in.listDataToWrite != null)
    // {
    // synchronized (in.listDataToWrite)
    // {
    // in.listDataToWrite.clear();
    // in.listDataToWrite.notifyAll();
    // }
    // }
    //
    // synchronized (ApplogManager.class)
    // {
    // instance = null;
    // }
    // }
    //
    // }

    public void init(Context context)
    {
        try
        {
            format = new SimpleDateFormat("MM-dd HH:mm:ss");
            this.context = context.getApplicationContext();
            listDataToWrite = new LinkedList<String>();
            applogInfo = new ApplogInfo();
            cacheLog = new File(context.getCacheDir() + "/applog.log");
            cacheLogSaved = new File(context.getCacheDir() + "/applog.tmp");
            if (!cacheLog.exists())
            {
                // if (cacheLog.length() > 50 * 1024)
                // {
                // cacheLog.delete();
                // }
                // }
                // else
                // {
                cacheLog.createNewFile();
            }
            inited = true;
        }
        catch (Exception e)
        {
            // ignore;
        }
    }

    private void invokeLogcat(String value)
    {
        writeString(getTimeTag() + TAG_LOGCAT + value);
    }

    private void invokePlayer(String value)
    {
        writeString(getTimeTag() + TAG_PLAY + value);
    }

    private void invokeDownload(String value)
    {
        writeString(getTimeTag() + TAG_DOWNLOAD + value);
    }

    private void invokeCrash(String value)
    {
        writeString(getTimeTag() + TAG_CRASH + value);
    }
    
    private ApplogManager()
    {
    }

    /**
     * 写入单条日志
     */
    private void writeString(String str)
    {
        try
        {
            synchronized (listDataToWrite)
            {
                // 防止日志太多太占内存
                if (listDataToWrite.size() > 200)
                {
                    listDataToWrite.clear();
                }

                listDataToWrite.add(str);
                listDataToWrite.notifyAll();
            }
            if (thread == null)
            {
                synchronized (WriteFileThread.class)
                {
                    if (thread == null)
                    {
                        thread = new WriteFileThread();
                        thread.start();
                    }
                }
            }
        }
        catch (Exception e)
        {
            // ignore
        }
    }

    private void invokeAppStart()
    {
        applogInfo.startTime = System.currentTimeMillis();
        writeString(getTimeTag() + TAG_START + "app start");
    }

    private void invokeAppStartEnd()
    {
        applogInfo.startEndTime = System.currentTimeMillis();
        writeString(getTimeTag() + TAG_START + "app ok");
    }

    @SuppressWarnings("unused")
	private void invokeLogin()
    {
//        writeString(getTimeTag() + TAG_PASSPORT + "user login:" + AccountPreferences.getUsername(context));
    }

    private void invokeLogout()
    {
        writeString(getTimeTag() + TAG_PASSPORT + "user logout");
    }

    private void invokeHttpRequest(String str)
    {
        writeString(getTimeTag() + TAG_HTTP + str);
    }

    private String getTimeTag()
    {
        return String.format("[%s] ", format.format(new java.util.Date(System.currentTimeMillis())));
    }

    private SimpleDateFormat format;

    private void writePassportInfo(BufferedWriter os) throws IOException
    {
//        if (os != null)
//        {
//            String username = AccountPreferences.getUsername(context);
//            String token = AccountPreferences.getLoginToken(context);
//            boolean loginState = AccountPreferences.getLogin(context);
//            User u = LoginService.parseUser(context, AccountPreferences.getVip(context));
//            boolean isVip = u != null && u.isVIP;
//            os.newLine();
//            os.write(getTimeTag() + TAG_PASSPORT
//                    + String.format("username:%s, islogin:%s, isVip:%s", username, loginState, isVip));
//            os.newLine();
//            os.write(getTimeTag() + TAG_PASSPORT + String.format("token:%s", token));
//
//            WAYGet get = WAYDatabaseHelper.getInstance(context).queryGet();
//            if (get != null)
//            {
//                String coolUser = ParseUtil.parseInt(CacheDatabaseHelper.getUserLevel(context)) >= 1 ? "1" : "0";
//                String area = get.areacode + "";
//                os.newLine();
//                os.write(getTimeTag() + TAG_PASSPORT + String.format("coolUser:%s, areacode:%s", coolUser, area));
//            }
//        }
    }

    private void closeStream(Closeable stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    /**
     * 写日志的线程，单线程处理
     */
    private class WriteFileThread extends Thread
    {
        public WriteFileThread()
        {
            setName("applog_thread");
        }

        @Override
        public void run()
        {
            while (true)
            {
                String firstLine = "";
                synchronized (listDataToWrite)
                {
                    if (listDataToWrite.isEmpty())
                    {
                        try
                        {
                            // 10秒内没有写入就关闭流
                            listDataToWrite.wait(10000);
                            if (listDataToWrite.isEmpty())
                            {
                                break;
                            }
                        }
                        catch (Exception e)
                        {
                            break;
                        }
                    }
                    else
                    {
                        try
                        {
                            firstLine = listDataToWrite.remove();
                        }
                        catch (Exception e)
                        {
                            break;
                        }
                    }
                }

                try
                {
                    if (cacheLog.length() > 100 * 1024)
                    {
                        closeStream(fos);
                        if (cacheLogSaved.exists())
                        {
                            cacheLogSaved.delete();
                        }
                        cacheLog.renameTo(cacheLogSaved);
                        cacheLog.createNewFile();
                    }

                    if (fos == null)
                    {
                        fos = new FileOutputStream(cacheLog, true);
                        bw = new BufferedWriter(new OutputStreamWriter(fos));
                    }

                    if (TextUtils.isEmpty(firstLine))
                    {
                        continue;
                    }
                    else
                    {
                        bw.newLine();
                        bw.write(firstLine);
                        bw.flush();
                    }
                }
                catch (Exception e)
                {
                    break;
                }
            }

            synchronized (WriteFileThread.class)
            {
                closeStream(fos);
                fos = null;
                thread = null;
            }
        }
    }

    private class ApplogInfo
    {
        // ---基本信息--
        public String phoneName;

        public String phoneVerison;

        public String appVersionName;

        public String appVersionCode;

        // ---启动--
        public long startTime;

        public long startEndTime;

        public String startType;

        public String imei;

        public String uuid;

        public String mac;

        // ---passport--
        // 暂时取username,token,登录状态

        // a) 账号登陆或者退出操作
        //
        // b) 发起登陆的passport完整URL（需要加密）
        //
        // c) 从passport返回的完整信息
        //
        // d) 获取到的部分信息记录，包括 用户名称、VIP状态等等。
        //
        // e) 如果因为网络原因失败，需要记录对应域名的目标IP（如果有）。

        // 下载信息跟播放信息，在执行的时候记录

        public ApplogInfo()
        {
            try
            {
                phoneName = "deviceinfo:" + Build.MANUFACTURER + "|" + Build.MODEL + "|" + Build.DEVICE;

                phoneVerison = "sdkversion:" + Build.VERSION.SDK;
                try
                {
                    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    appVersionCode = "appcode:" + info.versionCode;
                    appVersionName = "appversion:" + info.versionName;
                }
                catch (Exception e)
                {
                    // ignore
                }
//                uuid = String.format("UUID:%s", UUIDDatabaseHelper.getInstance(context).getUUID());
//                imei = String.format("IMEI:%s", DeviceInfo.getIMEI(context));
                mac = String.format("MAC:%s", NetworkUtils.getMacAddress(context));
            }
            catch (Exception e)
            {
                // ignore
            }
        }

        // ---其他信息
    }
}

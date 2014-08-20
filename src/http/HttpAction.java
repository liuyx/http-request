package http;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import utils.LogUtils;
import utils.Maps;
import utils.NetworkUtils;
import utils.PreConditions;
import utils.Resources;
import utils.Strings;
import utils.ThreadPool2;
import utils.ToastUtil;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * User: youxueliu
 * Date: 13-8-29
 * Time: 下午4:53
 * 所有Http请求的基类，该类封装了网络状态判断，然后用指定方法连接HTTP请求。
 * 连接过程中，将任务加到线程池中
 * 并将结果用Handler返回至UI
 */
public class HttpAction
{
    public static final int CONN_TIME_OUT = 30 * 1000;  // 连接超时时间
    public static final int READ_TIME_OUT = 30 * 1000; // 读写超时时间
    private static final String TAG = "HttpAction";

    private boolean mNoThread;

    public static final class HTTP
    {
        public static final String GET    = "GET";
        public static final String POST   = "POST";
        public static final String PUT    = "PUT";
        public static final String DELETE = "DELETE";
    }


    private OutputStream mOutputStream;
    private InputStream mInputStream;

    /**
     * 重写ContentType
     * @return
     */
    protected String getContentType()
    {
        return "application/x-www-form-urlencoded";
    }

    protected String getHttpMethod()
    {
        return HTTP.GET;
    }
    
    protected int getConnTime()
    {
        return CONN_TIME_OUT;
    }
    
    protected int getReadTime()
    {
        return READ_TIME_OUT;
    }

    /**
     * 测试Http请求条件是否允许，包括传进来的连接和网络
     * @param context
     * @param actionObj
     */
    private boolean checkEnv(Context context,ActionObj actionObj)
    {
        PreConditions.checkNotNull(actionObj);
        if(Strings.isNullOrEmpty(actionObj.mUrl))throw new RuntimeException("url is empty");

        if(!NetworkUtils.isNetworkAvailable(context))
        {
            ToastUtil.showShortMsg(context,"网络暂时不行，请稍后再试");
            if(actionObj.mHttpListener != null)
            {
                actionObj.mHttpListener.onFail(new SocketException("网络异常"));
            }
            return false;
        }
        return true;
    }

    public void action(Context context,final ActionObj actionObj)
    {
        if(!checkEnv(context,actionObj)) return;

        // 是否使用子线程，默认使用子线程
        final boolean noThread = mNoThread;

        if (noThread)
        {
            work(actionObj);
        }

        else
        {
            ThreadPool2.add(getCommonFeature(),new Runnable()
            {
                @Override
                public void run()
                {
                    work(actionObj);
                }
            });
        }

    }


    private MyHandler mHandler;

    public HttpAction()
    {
        mHandler = new MyHandler();
    }

    public HttpAction(boolean noThread)
    {
        mNoThread = noThread;
        if (!mNoThread)
            mHandler = new MyHandler();
    }

    protected boolean isUseHttps()
    {
        return false;
    }
    
    /**
     * 4.0以下手机put请求必须重写
     * @return {@link String} Content-Length
     * @see [类、类#方法、类#成员]
     */
    protected String getContentLength()
    {
        return null;
    }

    private void setDefaultHostnameVerifier()
    {
        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    protected boolean isWriteStream()
    {
        return false;
    }

    protected String getCommonFeature()
    {
        return null;
    }

    protected void work(ActionObj actionObj)
    {
        URLConnection conn = null;
        try{
            if(actionObj != null){

                conn = openConnection(actionObj);

                if (conn == null) throw new IllegalStateException("connection is null");

                setUrlConnectionAttrs(actionObj,conn);

                conn.connect();

                // 设置输出流
                sendBodyIfHas(actionObj, conn);

                readInputStream(actionObj,conn,getRespCode(conn));
            }
        }catch (Exception e){
            e.printStackTrace();
            //LogUtils.error(e.getCause().toString());
            LogUtils.error(TAG + "exception while execute " + actionObj.mFinalUrl + ", " + e);
            if (mHandler != null)
                mHandler.sendFailMsg(e,actionObj.mHttpListener);
            else
                actionObj.mHttpListener.onFail(e);
            HttpURLConnection connection = (HttpURLConnection) conn;
            final String string = Resources.toString(connection.getErrorStream());
            LogUtils.error(TAG + string);
        }finally {
            close(mInputStream);
            close(mOutputStream);
        }
    }

    private int getRespCode(URLConnection connection)
    {
        if (connection instanceof  HttpURLConnection)
        {
            try {
                final int respCode = ((HttpURLConnection)connection).getResponseCode();
                return respCode;
            }catch (Exception e){
                // ignore
            }
        }
        return -1;
    }

    /**
     * 连接请求
     * @param actionObj
     * @return
     */
    private URLConnection openConnection(ActionObj actionObj)
    {
        String urlStr = actionObj.mUrl;
        if (Strings.isNullOrEmpty(urlStr)) return null;
        try {
            // 组装url
            urlStr = actionObj.genGetUrl(urlStr,actionObj.mParamsMap);
            actionObj.mFinalUrl = urlStr;
            URL url = new URL(urlStr);
            LogUtils.debug(TAG+urlStr);
            return url.openConnection();
        }catch (Exception e){
            LogUtils.error(TAG + e.toString());
        }

        return null;
    }

    private void setUrlConnectionAttrs(ActionObj actionObj,URLConnection conn)
    {
        setHeader(actionObj,conn);

        setHttpMethod(conn);

        // 设置超时时间
        setTime(conn);
    }

    private void setHeader(ActionObj actionObj,URLConnection conn)
    {
        setHeaderParams(conn,actionObj.mHeaderParams);
        conn.setRequestProperty("Content-Type",getContentType());
        if (getContentLength() != null)
        {
            conn.setRequestProperty("Content-Length", getContentLength());
        }
    }

    private void setHttpMethod(URLConnection conn)
    {
        try {
        if (isUseHttps())
        {
            setDefaultHostnameVerifier();
            ((HttpsURLConnection)conn).setRequestMethod(getHttpMethod());
        }
        else
            ((HttpURLConnection)conn).setRequestMethod(getHttpMethod());
        }catch (ProtocolException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置连接超时时间和读取时间
     * @param conn
     */
    private void setTime(URLConnection conn)
    {
        conn.setConnectTimeout(getConnTime());
        conn.setReadTimeout(getReadTime());
        conn.setDoInput(true);
        final String httpMethod = getHttpMethod();
        if (!Strings.isNullOrEmpty(getHttpMethod()) && !httpMethod.equals(HTTP.GET) && !httpMethod.equals(HTTP.DELETE))
            conn.setDoOutput(true);
    }

    private void sendBodyIfHas(ActionObj actionObj, URLConnection conn)
    {
        boolean isGet = getHttpMethod().equals(HTTP.GET);
        boolean isDel = getHttpMethod().equals(HTTP.DELETE);
        boolean writeAsStr = !Strings.isNullOrEmpty(actionObj.mBodyStr);
        boolean writeAsStream = isWriteStream() & (actionObj.mBodyInputStream != null);

        try {
            if (!isGet & !isDel & (writeAsStr | writeAsStream))
            {
                mOutputStream = new DataOutputStream(conn.getOutputStream());
                if(writeAsStr)
                    mOutputStream.write((actionObj.mBodyStr).getBytes());

                else if(writeAsStream)
                    write(actionObj.mBodyInputStream,mOutputStream,actionObj.mToBeReadSize,actionObj.mCopyStreamListener,actionObj.mActivity,getCommonFeature());
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void readInputStream(ActionObj actionObj,URLConnection conn,int respCode)
    {
        try {
            mInputStream = conn.getInputStream();
            Map<String,List<String>> headers =  conn.getHeaderFields();
            if (mInputStream != null)
            {
                if (mNoThread)
                {
                    actionObj.mHttpListener.onSuccess(Resources.toString(mInputStream),respCode);
                }
                else if (mHandler != null)
                {
                    mHandler.sendSuccessMsg(Resources.toString(mInputStream),respCode,headers,actionObj.mHttpListener);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void write(InputStream is,OutputStream out,long toBeReadSize,Resources.CopyStreamListener listener,Activity activity,String taskComFeature)
    {
        if (toBeReadSize == -1L)
            Resources.copy(is,out);
        else if (toBeReadSize > 0L)
            Resources.copy(is,out,toBeReadSize,listener,activity,taskComFeature);
    }

    private void close(Closeable c)
    {
        if(c != null)
        {
            try{
                c.close();
            }catch (IOException e){
                e.printStackTrace();
                LogUtils.error(e.toString());
            }
        }
    }


    private void setHeaderParams(URLConnection conn,Map<String,String> headParams)
    {
        if(headParams != null)
        {
            for(Map.Entry<String,String> entry : headParams.entrySet())
            {
                if(entry != null && entry.getKey() != null && entry.getValue() != null)
                    conn.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }
    }

    private static class MyHandler extends Handler
    {
        private static final int ON_SUCCESS = 0;
        private static final int ON_FAIL = 1;

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case ON_SUCCESS:
                    onSuccess(msg);
                    break;
                case ON_FAIL:
                    onFail(msg);
                    break;
                default:
                    throw new RuntimeException("只有失败和成功的状态");
            }
        }

        private Bundle strListStrMapToBundle(Map<String,List<String>> headers)
        {
            if(headers == null) return null;
            final Bundle result = new Bundle();
            for(String key : headers.keySet()){
                result.putStringArrayList(key,listToArrayList(headers.get(key)));
            }
            return result;
        }

        private ArrayList<String> listToArrayList(List<String> src)
        {
            if(src == null)return null;
            ArrayList<String> result = new ArrayList<String>(src.size());
            final int len = src.size();
            for(int i = 0; i < len; i++)
                result.add(src.get(i));
            return result;
        }

        private void sendMsg(int what, Object obj,int respCode,Map<String,List<String>> headers,HttpActionListener listener)
        {
            Message msg = Message.obtain();
            msg.obj = obj;
            Bundle data = new Bundle();
            data.putSerializable("listener", listener);
            data.putInt("respCode",respCode);
            final Bundle headersBundle = strListStrMapToBundle(headers);
            if(headersBundle != null)
                data.putBundle("headers",headersBundle);
            msg.setData(data);
            msg.what = what;
            sendMessage(msg);
        }

        private void sendSuccessMsg(String content,int respCode,Map<String,List<String>> headers,HttpActionListener listener)
        {
            sendMsg(MyHandler.ON_SUCCESS, content,respCode,headers,listener);
        }

        private void sendFailMsg(Throwable thr,HttpActionListener listener)
        {
            sendMsg(MyHandler.ON_FAIL, thr,-1,null,listener);
        }

        private HttpActionListener getListener(Message msg)
        {
            final Bundle data = msg.getData();
            if(data == null) return null;
            return (HttpActionListener)data.getSerializable("listener");
        }

        private void onSuccess(Message msg)
        {
            final String content = (String)msg.obj;
            int respCode = -1;
            final HttpActionListener listener = getListener(msg);
            final Bundle data = msg.getData();
            if(data != null)
            {
                final Bundle headers = data.getBundle("headers");
                respCode = data.getInt("respCode");
                if(headers != null && content != null && listener != null)
                    listener.onSuccessHeaders(content, respCode, bundleToMap(headers));
            }
            if(content != null && listener != null)
                listener.onSuccess(content, respCode);
        }

        private void onFail(Message msg)
        {
            final Throwable thr = (Throwable)msg.obj;
            final HttpActionListener listener = getListener(msg);
            if(thr != null && listener != null)
                listener.onFail(thr);
        }

        private Map<String,List<String>> bundleToMap(Bundle data)
        {
            final Map<String,List<String>> result = Maps.newHashMap();
            for(String key : data.keySet())
                result.put(key,data.getStringArrayList(key));
            return result;
        }}

    /**
     * 提交使用Http请求的对象
     */
    public static class ActionObj
    {
        public final String mUrl;
        public final Map<String,String> mParamsMap;                                     // url的参数
        public final HttpActionListener mHttpListener;                                  // 执行http请求后回调的监听器
        public final Map<String,String> mHeaderParams;                                  // 头部参数
        public final String mBodyStr;                                                   // body参数
        public InputStream mBodyInputStream;                                            // body中输出流
        private long mToBeReadSize = -1;                                                // 输出流中读取的字节数，-1表示全部读取直到流结束
        private final Resources.CopyStreamListener mCopyStreamListener;                // 监听流读写的监听器
        private final Activity mActivity;                                              // activity用于显示界面的东西
        @SuppressWarnings("unused")
		private final boolean mNoThread;

        private String mFinalUrl;

        public ActionObj(String url, Map<String, String> paramsMap, HttpActionListener listener)
        {
            this(url,null,paramsMap,listener);
        }

        public ActionObj(String url, Map<String, String> headerParams, Map<String, String> paramsMap, HttpActionListener listener)
        {
            this(url,headerParams,paramsMap,null,listener);
        }


        public ActionObj(String url, Map<String, String> headerParams, Map<String, String> paramsMap, String bodyStr,HttpActionListener listener)
        {
            this(url,headerParams,paramsMap,bodyStr,null,listener);
        }

        public ActionObj(String url, Map<String, String> headerParams, Map<String, String> paramsMap,String bodyStr, InputStream is,HttpActionListener listener)
        {
            this(url,headerParams,paramsMap,bodyStr,is,-1,listener);
        }

        public ActionObj(String url, Map<String, String> headerParams, Map<String, String> paramsMap,String bodyStr, InputStream is,long toBeReadSize,HttpActionListener listener)
        {
            this(url,headerParams,paramsMap,bodyStr,is,toBeReadSize,listener,null);
        }

        public ActionObj(String url, Map<String, String> headerParams, Map<String, String> paramsMap,String bodyStr, InputStream is,long toBeReadSize,HttpActionListener listener,Resources.CopyStreamListener listener2)
        {
            this(url,headerParams,paramsMap,bodyStr,is,toBeReadSize,listener,listener2,null);
        }

        public ActionObj(String url, Map<String, String> headerParams, Map<String, String> paramsMap,String bodyStr, InputStream is,long toBeReadSize,HttpActionListener listener,Resources.CopyStreamListener listener2,Activity activity)
        {
            this(url,headerParams,paramsMap,bodyStr,is,toBeReadSize,listener,listener2,activity,false);
        }

        public ActionObj(String url, Map<String, String> headerParams, Map<String, String> paramsMap,String bodyStr, InputStream is,long toBeReadSize,HttpActionListener listener,Resources.CopyStreamListener listener2,Activity activity,boolean noThread)
        {
            mUrl = url;
            mParamsMap = paramsMap;
            mHttpListener = listener;
            mBodyStr = bodyStr;
            mHeaderParams = headerParams;
            mBodyInputStream = is;
            mToBeReadSize = toBeReadSize;
            mCopyStreamListener = listener2;
            mActivity = activity;
            mNoThread = noThread;
        }

        public String genGetUrl(String baseUrl,Map<String,String> params) throws UnsupportedEncodingException
        {
            if(baseUrl == null || params == null)return baseUrl;
            StringBuilder sb = new StringBuilder(baseUrl + appendStr());
            sb.append(mapToStr(params,isEncoded()));
            return sb.toString();
        }

        /**
         * baseUrl中假如没有接?号，此处应该接?号，否则，接&号
         * @return
         */
        protected String appendStr()
        {
            return "?";
        }

        /**
         * 默认是否对参数进行Encode，默认为true
         * @return
         */
        protected boolean isEncoded()
        {
            return true;
        }

        public static String mapToStr(Map<String,String> params,boolean encode) throws UnsupportedEncodingException
        {
            if(params == null)return "";
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String,String> entry : params.entrySet())
            {
                if (encode)
                    sb.append(entry.getKey()+"="+ URLEncoder.encode(entry.getValue(),"UTF-8")).append("&");
                else
                    sb.append(entry.getKey() + "=" + entry.getValue()).append("&");
            }
            sb.delete(sb.length()-1,sb.length());

            return sb.toString();
        }
    }

    /**
     *  提交Http请求成功后的监听器
     */
    public static abstract class HttpActionListener implements Serializable
    {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public void onSuccess(String content, int respCode){}
        public void onFail(Throwable throwable){}
        public void onSuccessHeaders(String content, int respCode, Map<String, List<String>> headers){}
        public void onSuccessNotUI(String content){}
    }
}

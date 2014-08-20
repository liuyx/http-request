package http;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import utils.LogUtils;
import utils.Resources;
import utils.Strings;
import android.app.Activity;
import android.content.Context;

/**
 * User: youxueliu
 * Date: 13-11-13
 * Time: 下午12:58
 * 所有Http与UI请求的基类
 * 默认对所有的参数进行了Encode，若不需要Encode，可以重写isEncode方法，并return false
 * 默认起了线程，多不想起子线程，可以重写noThread方法，并返回false
 */
public abstract class HttpService
{
    protected Context mContext;
    protected String mBaseUrl;
    public HttpService(Context context)
    {
        mContext = context;
        mBaseUrl = getBaseUrl();
    }

    /**
     * 返回baseUrl
     * @return
     */
    protected abstract String getBaseUrl();

    /**
     * 返回请求的参数，默认对所有的参数进行了Encode，若不需要Encode，可以重写isEncode方法，并return false
     * @return
     */
    protected Map<String,String> genParams(){ return null; }

    /**
     * 返回Header参数
     * @return
     */
    protected Map<String,String> genHeaderParams(){ return null; }

    protected boolean noThread() { return false; }

    /**
     * 需要传到body的内容
     * @return
     */
    protected String genBodyStr() { return null; }

    protected InputStream genBodyInputStream (){ return null; }

    /**
     * 将要读取多少字节，-1表示全部读取直到流结束
     * @return
     */
    protected long getToBeReadSize() { return -1L; }


    /**
     * 微调baseUrl
     * @return 返回微调之后的结果，如将REST格式的url路径中需要的变量进行替换
     * 例如,xxx/{fid}/xxx/xxx  替换fid的结果为xxx/1/xxx/xxx
     */
    protected String adjustBaseUrl(String baseUrl)
    {
        return baseUrl;
    }

    /**
     * 重写ContentType
     * @return
     */
    protected String getContentType()
    {
        return "application/x-www-form-urlencoded";
    }
    
    protected String getContentLength()
    {
        return null;
    }

    protected String getHttpMethod()
    {
        return HttpAction.HTTP.GET;
    }

    protected String getAppendStr()
    {
        return "?";
    }

    /**
     * 默认是否对参数进行Encode，默认为true
     * @return
     */
    protected boolean isEncode()
    {
        return true;
    }

    protected boolean isUseHttps()
    {
        return false;
    }

    protected boolean isWriteStream()
    {
        return false;
    }

    /**
     * 用于PUT请求
     * @param bytes
     */
    protected void onByte(long bytes)
    {
    }

    /**
     * Http请求被取消
     */
    protected void onCancel()
    {

    }

    protected Activity getActivity()
    {
        return null;
    }

    /**
     * 用于多个任务协同进行
     * @return
     */
    protected String getCommonFeature()
    {
        return null;
    }


    protected void action(final HttpAction.HttpActionListener listener)
    {
        if(Strings.isNullOrEmpty(mBaseUrl)) return;
        final String url = adjustBaseUrl(mBaseUrl);
        if (Strings.isNullOrEmpty(url)) return;
        final boolean noThread = noThread();
        new HttpAction(noThread){
            @Override
            protected String getContentType()
            {
                return HttpService.this.getContentType();
            }

            @Override
            protected String getHttpMethod()
            {
                return HttpService.this.getHttpMethod();
            }

            @Override
            protected boolean isUseHttps() {
                return HttpService.this.isUseHttps();
            }

            @Override
            protected boolean isWriteStream()
            {
                return HttpService.this.isWriteStream();
            }

            @Override
            protected String getCommonFeature()
            {
                return HttpService.this.getCommonFeature();
            }

            protected String getContentLength()
            {
                return HttpService.this.getContentLength();
            }

        }.action(mContext, new HttpAction.ActionObj(url, genHeaderParams(), genParams(), genBodyStr(),genBodyInputStream(),getToBeReadSize(), new HttpAction.HttpActionListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void onSuccess(String content, int respCode) {
                super.onSuccess(content, respCode);
                if (listener != null)
                {
                    listener.onSuccess(content, respCode);
                    LogUtils.debug(content);
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                super.onFail(throwable);

                if (listener != null && throwable != null)
                    listener.onFail(throwable);
            }

            @Override
            public void onSuccessHeaders(String content, int respCode, Map<String, List<String>> headers) {
                super.onSuccessHeaders(content, respCode, headers);
                if (listener != null && content != null && headers != null)
                    listener.onSuccessHeaders(content, respCode, headers);
            }

            @Override
            public void onSuccessNotUI(String content) {
                super.onSuccessNotUI(content);
                if (listener != null && !Strings.isNullOrEmpty(content))
                    listener.onSuccessNotUI(content);
            }
        },new Resources.CopyStreamListener()
        {
            @Override
            public void onByte(long bytes)
            {
                HttpService.this.onByte(bytes);
            }

            @Override
            public void onCancel()
            {

            }
        },getActivity())

        {
            @Override
            protected String appendStr()
            {
                return getAppendStr();
            }

            @Override
            protected boolean isEncoded()
            {
                return HttpService.this.isEncode();
            }
        });
    }
}

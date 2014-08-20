package http;

import http.listener.HttpResultListener;

import java.util.List;
import java.util.Map;

import android.content.Context;

/**
 * User: youxueliu
 * Date: 13-11-11
 * Time: 下午2:30
 */
public abstract class HttpPostService extends HttpService {
    public HttpPostService(Context context,String baseUrl)
    {
        super(context);
        mBaseUrl = baseUrl;
    }

    public HttpPostService(Context context)
    {
        super(context);
    }

    @Override
    protected String getHttpMethod()
    {
        return HttpAction.HTTP.POST;
    }

    protected String getBaseUrl()
    {
        return null;
    }

    protected String genBodyStr()
    {
        return null;
    }

    /**
     * @param baseUrl
     * 微调baseUrl
     * @return 返回微调之后的结果，如将REST格式的url路径中需要的变量进行替换
     * 例如,xxx/{fid}/xxx/xxx  替换fid的结果为xxx/1/xxx/xxx
     */
    protected String adjustBaseUrl(String baseUrl)
    {
        return baseUrl;
    }

    @Override
    protected Map<String, String> genParams()
    {
        return HttpParameters.bundleToMap(HttpParameters.getCommonParams(mContext));
    }


    public void post(@SuppressWarnings("rawtypes") final HttpResultListener listener)
    {
        if (listener == null) return;
        action(new HttpAction.HttpActionListener()
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
            public void onSuccess(String content, int respCode)
            {
                super.onSuccess(content, respCode);
                if (listener != null)
                    listener.onSuccess(content);
            }

            @Override
            public void onFail(Throwable throwable)
            {
                super.onFail(throwable);
                if (listener != null)
                    listener.onFail(throwable);
            }

            @Override
            public void onSuccessHeaders(String content, int respCode, Map<String, List<String>> headers)
            {
                super.onSuccessHeaders(content, respCode, headers);
            }

            @Override
            public void onSuccessNotUI(String content)
            {
                super.onSuccessNotUI(content);
            }
        });
    }

}

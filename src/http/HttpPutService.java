package http;

import http.listener.HttpResultListener;

import java.util.List;
import java.util.Map;

import android.content.Context;

/**
 * User: youxueliu
 * Date: 13-11-13
 * Time: 下午2:22
 */
public class HttpPutService extends HttpService
{
    @Override
    protected String getBaseUrl()
    {
        return null;
    }

    @Override
    protected String getHttpMethod()
    {
        return HttpAction.HTTP.PUT;
    }

    public HttpPutService(Context context)
    {
        super(context);
    }

    /**
     * 处理头不常见，故放在此:
     * @param content
     * @param headers
     */
    public void onSuccessHeaders(String content, Map<String, List<String>> headers)
    {
    }

    @Override
    protected Map<String, String> genParams()
    {
        return HttpParameters.bundleToMap(HttpParameters.getCommonParams(mContext));
    }


    public void put(@SuppressWarnings("rawtypes") final HttpResultListener listener)
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
                HttpPutService.this.onSuccessHeaders(content, headers);
            }

            @Override
            public void onSuccessNotUI(String content)
            {
                super.onSuccessNotUI(content);
            }
        });
    }
}

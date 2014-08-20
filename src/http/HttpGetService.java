package http;

import http.exception.ParseModelException;
import http.listener.HttpResultAction;
import http.listener.HttpResultActionWithHead;

import java.util.List;
import java.util.Map;

import android.content.Context;


/**
 * User: youxueliu
 * Date: 13-10-22
 * Time: 下午3:51
 * 获取Http请求类
 */
@SuppressWarnings("rawtypes")
public class HttpGetService<T extends HttpRespModel> extends HttpService
{
    /**
     * Http请求的监听器
     */
    protected HttpResultAction<T> mHttpResultAction;
    public HttpGetService(Context context, String baseUrl)
    {
        super(context);
        mBaseUrl = baseUrl;
    }

    public HttpGetService(Context context)
    {
        super(context);
        mBaseUrl = getBaseUrl();
    }

    protected String getBaseUrl()
    {
        return null;
    }
    

    @Override
    protected String getHttpMethod()
    {
        return HttpAction.HTTP.GET;
    }

    /**
     * 获取http参数的map对象
     * @return
     */
    protected Map<String,String> genParams()
    {
       return HttpParameters.bundleToMap(HttpParameters.getCommonParams(mContext));
    }

    /**
     * 在调用get之前调用的方法
     */
    protected void preGet(){}

    /**
     * 在调用get之后调用的方法
     */
    protected void postGet(){}


    /**
     * http get请求
     * @param cls 服务端返回值封装成bean的class对象
     * @param httpResultAction 执行http get之后回调的接口
     */
    @SuppressWarnings("serial")
	public void get(final Class<T> cls,final HttpResultAction<T> httpResultAction)
    {
        preGet();
        if(cls == null || httpResultAction == null ) return;
        mHttpResultAction = httpResultAction;
        action(new HttpAction.HttpActionListener() {
            @SuppressWarnings("unchecked")
			@Override
            public void onSuccess(String content, int respCode) {
                super.onSuccess(content, respCode);
                try{
                    T model = cls.newInstance();
                    model = (T) model.getRespModel(content);
                    if(model != null)
                        httpResultAction.onSuccess(model);
                    else
                        httpResultAction.onFail(new ParseModelException("parse model exception"));
                }catch (Exception e){
                    httpResultAction.onFail(e);
                }
                postGet();
            }

            @Override
            public void onFail(Throwable throwable) {
                super.onFail(throwable);
                if(throwable != null)
                    httpResultAction.onFail(throwable);

                postGet();
            }

            @SuppressWarnings({ "unchecked" })
			@Override
            public void onSuccessHeaders(String content, int respCode, Map<String, List<String>> headers)
            {
                super.onSuccessHeaders(content, respCode, headers);
                if (httpResultAction instanceof  HttpResultActionWithHead)
                {
                    ((HttpResultActionWithHead)httpResultAction).onSuccess(content,respCode,headers);
                }
            }

            @Override
            public void onSuccessNotUI(String content)
            {
                super.onSuccessNotUI(content);
            }
        });

    }



}

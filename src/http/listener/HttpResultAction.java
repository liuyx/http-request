package http.listener;

import http.HttpRespModel;
import utils.LogUtils;

/**
 * User: youxueliu
 * Date: 13-10-22
 * Time: 下午3:49
 * 执行http请求后执行的结果,默认将错误详细打印在Log中
 */
@SuppressWarnings("rawtypes")
public abstract class HttpResultAction<T extends HttpRespModel> implements HttpResultListener<T>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public abstract void onSuccess(T result);

    /**
     * 出现异常时，默认是打印日志
     * @param thr
     */
    public void onFail(Throwable thr){
        if(thr != null)
            LogUtils.error(thr.toString());
    }
}

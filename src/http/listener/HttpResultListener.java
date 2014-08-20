package http.listener;

import java.io.Serializable;

/**
 * 执行http请求后回调的监听器 <一句话功能简述> <功能详细描述>
 * 
 * @author vivili
 * @version [版本号, 2013-10-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface HttpResultListener<T> extends Serializable
{
    /**
     * OK
     */
    int OK = 200;

    int CREATED = 201;

    /**
     * 服务端内容没有变化，使用本地缓存
     */
    int NOT_MODIFY = 304;


    /**
     * 执行http请求成功返回回调的方法
     * 
     * @param result 返回的bean
     * @see [类、类#方法、类#成员]
     */
    void onSuccess(T result);

    /**
     * 执行http请求过程中出现了异常回调的方法
     * 
     * @param thr
     * @see [类、类#方法、类#成员]
     */
    void onFail(Throwable thr);
    
    /**
     * Used only for tackle exception issue, don't care the success return model
     * @author youxueliu
     *
     */
    static class NULL 
    {
    	
    }

}

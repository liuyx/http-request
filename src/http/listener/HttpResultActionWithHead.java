package http.listener;

import http.HttpRespModel;

import java.util.List;
import java.util.Map;

/**
 * 执行http请求后回调的监听器 <一句话功能简述> <功能详细描述>
 * 
 * @author vivili
 * @version [版本号, 2013-10-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@SuppressWarnings("rawtypes")
public abstract class HttpResultActionWithHead<T extends HttpRespModel> extends HttpResultAction<T>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void onSuccess(String content,int respCode,Map<String,List<String>> headers);

    /**
     * get Date
     * @param headers
     * @return
     */
    public static String getDate(Map<String, List<String>> headers)
    {
        final List<String> dates = headers.get("Date");
        if (dates != null && dates.size() == 1) return dates.get(0);
        return null;
    }
}

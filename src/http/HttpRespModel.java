package http;

/**
 * User: youxueliu
 * Date: 13-10-22
 * Time: 下午3:56
 * 执行Http请求后服务器响应的bean对象
 */
@SuppressWarnings("rawtypes")
public abstract class HttpRespModel<T extends HttpRespModel> {

    /**
     * 请求http之后返回response，response封装成HttpRespModel的子类
     * @param response
     * @return
     */
    public abstract T getRespModel(String response);
}

package http;

import org.json.JSONObject;

import utils.LogUtils;
import utils.Strings;

/**
 * User: youxueliu
 * Date: 14-3-3
 * Time: 下午5:07
 */
public class HttpGetString extends HttpRespModel<HttpGetString>
{
    private static final String ADMIN_ERR_CODE = "errorCode";

    /**
     * 调用getErrorCode时，json错误返回的默认码
     */
    public static final int ERR = -1;

    public static final int OK = 0;

    private String mString;

    public String getString()
    {
        return mString;
    }

    @Override
    public HttpGetString getRespModel(String response)
    {
        HttpGetString getString = new HttpGetString();
        getString.mString = response;
        return getString;
    }

    @Override
    public String toString()
    {
        return mString;
    }

    /**
     * 调用该方法发生异常时，返回ERR,否则，返回json的errorCode
     * @return
     */
    public int getErrorCode()
    {
        return getErrorCode(ADMIN_ERR_CODE);
    }

    /**
     * 调用该方法发生异常时，返回ERR,否则，返回json的errorCode
     * @return
     */
    public int getErrorCode(String errorKey)
    {
        if (Strings.isNullOrEmpty(mString)) return ERR;
        try {
            final JSONObject jsonObject = new JSONObject(mString);
            final int errorCode = jsonObject.optInt(errorKey);
            return errorCode;
        }catch (Exception e){
            LogUtils.debug("HttpGetString json error " + e);
        }
        return ERR;
    }
}

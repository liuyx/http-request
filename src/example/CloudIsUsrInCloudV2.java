package example;

import http.HttpGetService;
import http.HttpGetString;
import http.HttpParameters;

import java.util.Map;

import android.content.Context;

/**
 * User: youxueliu
 * Date: 2014/6/23
 * Time: 17:45
 * 5.2.2	用户账户信息接口V2
 */
public class CloudIsUsrInCloudV2 extends HttpGetService<HttpGetString>{
    public CloudIsUsrInCloudV2(Context context){
        super(context);
    }

    @Override
    protected String getBaseUrl() {
        return "the http url it's used";
    }

    @Override
    protected Map<String, String> genParams() {
        final Map<String,String> result = HttpParameters.PriCloudComParams.getComParams(mContext);
//        result.put("mn", DeviceInfo.getIMEI(mContext)); //设备名称
        result.put("mt","Aphone"); // 设备类型
        return result;
    }

    @Override
    protected boolean isEncode() {
        return false;
    }
}

package example.delete;

import http.HttpDeleteService;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

/**
 * User: youxueliu
 * Date: 14-2-28
 * Time: 上午11:36
 * 用户删除评论
 */
public class UsrDelVote extends HttpDeleteService
{
    @SuppressWarnings("unused")
	private final String mToken;
    private final String[] mVids;
    public UsrDelVote(Context context,String token,String[] vids)
    {
        super(context);
        mToken = token;
        mVids = vids;
    }

    @Override
    protected String getBaseUrl()
    {
    	return "the http url it's used";
    }

    @Override
    protected Map<String, String> genParams()
    {
        final Map<String,String> paramsMap = super.genParams();
        final StringBuilder sb = new StringBuilder();
        for (String vid: mVids)
            sb.append(vid).append(",");
        sb.delete(sb.length()-1,sb.length());
        paramsMap.put("vids",sb.toString());
        return paramsMap;
    }

    @Override
    protected Map<String, String> genHeaderParams()
    {
    	Map<String,String> result =  new HashMap<String,String>(); // "map with key-value pair add to headers";
    	result.put("key","value");
    	return result;
    }

    @Override
    protected boolean isEncode()
    {
        return false;
    }
}

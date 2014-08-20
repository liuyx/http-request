package example.post;

import http.HttpPutService;
import http.listener.HttpResultListener;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.LogUtils;
import android.content.Context;
import example.get.VoteInfoBean;

/**
 * User: youxueliu
 * Date: 14-2-28
 * Time: 上午11:08
 * 用户发起投票
 */
public class UsrSendVote extends HttpPutService
{
    public static final class Params
    {
        @SuppressWarnings("unused")
		private String mToken;
        private String mRefName;
        private String[] mOptions;
        private String mUsrName;
        private String mTitle;
        private String mVoteType = "1";

        private String mPlatform = "pplive";

        public Params setRefName(String refName)
        {
            mRefName = refName;
            return this;
        }

        public Params setToken(String token)
        {
            mToken = token;
            return this;
        }

        public Params setOptions(String[] options)
        {
            mOptions = options;
            return this;
        }

        public Params setUsrName(String usrName)
        {
            mUsrName = usrName;
            return this;
        }

        public Params setTitle(String title)
        {
            mTitle = title;
            return this;
        }

        /**
         * @param voteType  {@link VoteInfoBean.VOTE_TYPE_SINGLE} or {@link VoteInfoBean.VOTE_TYPE_MULTIPLE}
         * @return
         * @see [类、类#方法、类#成员]
         */
        public Params setVoteType(String voteType)
        {
            mVoteType = voteType;
            return this;
        }
    }

    private final Params mParams;


    public UsrSendVote(Context context,Params params)
    {
        super(context);
        mParams = params;
    }

    @Override
    protected String getBaseUrl()
    {
    	return "the http url it's used";
    }

    private String getJSONStr()
    {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("refName",mParams.mRefName);
            final JSONArray jsonArray = new JSONArray();
            for (String option: mParams.mOptions)
                jsonArray.put(option);
            jsonObject.put("options",jsonArray);
            jsonObject.put("userName",mParams.mUsrName);
            jsonObject.put("platform",mParams.mPlatform);
            jsonObject.put("title",mParams.mTitle);
            jsonObject.put("voteType",mParams.mVoteType);
            return jsonObject.toString();
        }catch (JSONException e){
            LogUtils.debug(e.toString());
        }
        return null;
    }

    @Override
    protected String genBodyStr()
    {
        return getJSONStr();
    }

    @Override
    protected Map<String, String> genHeaderParams()
    {
    	Map<String,String> result =  new HashMap<String,String>(); // "map with key-value pair add to headers";
    	result.put("key","value");
    	return result;
    }

    public void send(@SuppressWarnings("rawtypes") HttpResultListener listener)
    {
        put(listener);
    }
}

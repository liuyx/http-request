package example.put;

import http.HttpPutService;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import utils.LogUtils;
import utils.ReplaceBraceValue;
import utils.Strings;
import android.content.Context;

/**
 * User: youxueliu
 * Date: 14-3-3
 * Time: 上午11:38
 * 发动态
 */
public class PostNewStateV2 extends HttpPutService
{
    public static final class Params
    {
        @SuppressWarnings("unused")
		private String mToken;
        private Type mType;
        private String mRefId;
        private String mReplyId;

        /**
         * 动态内容
         */
        private String mContent;

        private String mPlatform = "platform";

        public Params setToken(String token)
        {
            mToken = token;
            return this;
        }

        public Params setType(Type type)
        {
            mType = type;
            return this;
        }

        public Params setRefId(String refId)
        {
            mRefId = refId;
            return this;
        }

        public Params setReplyId(String replyId)
        {
            mReplyId = replyId;
            return this;
        }

        public Params setContent(String content)
        {
            mContent = content;
            return this;
        }

        public static enum Type
        {
            COMMENT("Comment"),              // 评论
            SHARE("Share"),                  // 分享
            UPLOAD("Upload"),                // 上传
            FOLLOW_CHANNEL("FollowChannel"); // 关注频道

            private String mCode;
            private Type(String code)
            {
                mCode = code;
            }

            public String getCode()
            {
                return mCode;
            }
        }
    }

    private final Params mParams;

    public PostNewStateV2(Context context,Params params)
    {
        super(context);
        mParams = params;
    }

    @Override
    protected String getBaseUrl()
    {
    	return "the http url it's used";
    }

    @Override
    protected String adjustBaseUrl(String baseUrl)
    {
        return ReplaceBraceValue.replace(baseUrl,mParams.mPlatform);
    }

    @Override
    protected String genBodyStr()
    {
        switch (mParams.mType)
        {
            case COMMENT:
                return getCommentBodyStr();
            case SHARE:
            case UPLOAD:
            case FOLLOW_CHANNEL:
                return getOtherTypeBodyStr();
        }
        return super.genBodyStr();
    }

    @Override
    protected Map<String, String> genHeaderParams()
    {
    	Map<String,String> result =  new HashMap<String,String>(); // "map with key-value pair add to headers";
    	result.put("key","value");
    	return result;
    }

    private String getCommentBodyStr()
    {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content",mParams.mContent);
            jsonObject.put("type",mParams.mType.getCode());
            jsonObject.put("refId",mParams.mRefId);
            if (!Strings.isNullOrEmpty(mParams.mReplyId))
                jsonObject.put("replyId",mParams.mReplyId);
            return jsonObject.toString();
        }catch (JSONException e){
            LogUtils.debug(e.toString());
        }
        return null;
    }

    private String getOtherTypeBodyStr()
    {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type",mParams.mType.getCode());
            return jsonObject.toString();
        }catch (JSONException e){
            LogUtils.debug(e.toString());
        }
        return null;
    }
}

package example.get;

import http.HttpGetService;

import java.util.Map;

import android.content.Context;

/**
 * User: youxueliu
 * Date: 14-2-28
 * Time: 上午11:44
 * 用户查看投票
 */
public class UsrCheckVotes extends HttpGetService<UsrCheckVotesModel>{
    private final String[] mVids;
    public UsrCheckVotes(Context context,String[] vids)
    {
        super(context);
        mVids = vids;
    }

    @Override
    protected String getBaseUrl() {
    	return "the http url it's used";
    }

    @Override
    protected Map<String, String> genParams() {
        final Map<String,String> result = super.genParams();
        final StringBuilder sb = new StringBuilder();
        for (String vid: mVids)
            sb.append(vid).append(",");
        sb.delete(sb.length()-1,sb.length());
        result.put("vids",sb.toString());
        return result;
    }

    @Override
    protected boolean isEncode() {
        return false;
    }
}

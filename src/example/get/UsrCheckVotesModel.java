package example.get;

import http.HttpRespModel;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Lists;
import utils.LogUtils;

public class UsrCheckVotesModel extends HttpRespModel<UsrCheckVotesModel> {

    private List<VoteInfoBean> mUsrVotes;

    public List<VoteInfoBean> getVoteInfos() {
        return mUsrVotes;
    }

	/*----------------------JSON parse--------------------------*/

    @Override
    public UsrCheckVotesModel getRespModel(String json){
        final UsrCheckVotesModel result = new UsrCheckVotesModel();
        try{
            JSONObject jsonObj = new JSONObject(json);
            result.mUsrVotes = getUsrVotes(jsonObj.optJSONArray("data"));
        }catch(JSONException e){
            LogUtils.error(e.toString());
        }

        return result;
    }

    private static List<VoteInfoBean> getUsrVotes(JSONArray jsonArray)
    {
        if (jsonArray == null) return null;
        final int len = jsonArray.length();
        List<VoteInfoBean> result = Lists.newArrayList();
        for (int i = 0; i < len; ++i)
            result.add(VoteInfoBean.getVoteInfoBean(jsonArray.optJSONObject(i)));
        return result;
    }

}

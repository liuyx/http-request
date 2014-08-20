package example.get;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Maps;

/**
 * User: youxueliu
 * Date: 14-3-4
 * Time: 下午2:02
 */
public class VoteInfoBean
{
    public static final String VOTE_TYPE_SINGLE = "0"; // 单选
    public static final String VOTE_TYPE_MULTIPLE = "1"; // 多选
    private int vid;
    private String userName;
    private String userNick;
    private String userIcon;
    private String refName;
    private String title;
    private List<VoteOption> ltVoteOption;
    private Map<String,String> mapOption;
    private int optionType;
    private int voteType;
    private int userType;
    private long playTime;
    private String startTime;
    private String endTime;
    private String platform;
    private long createTime;
    private long updateTime;
    private boolean delFlag;
    private int audit;
    private boolean auditFirst;
    private String from;
    private String version;
    private String ip;
    private String port;
    private String rule;
    private boolean award;
    private int totalUserCount;

    private Map<String, Integer> mapOptionCount;
    private boolean valid;
    private boolean del;

    public Map<String, String> getMapOption() {
        return mapOption;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public boolean isAuditFirst() {
        return auditFirst;
    }

    public boolean isAward() {
        return award;
    }

    public int getTotalUserCount() {
        return totalUserCount;
    }

    public Map<String, Integer> getMapOptionCount() {
        return mapOptionCount;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isDel() {
        return del;
    }

    public int getVid(){
        return vid;
    }

    public String getUserName(){
        return userName;
    }

    public String getUserNick(){
        return userNick;
    }

    public void setUserNick(String nick){
        this.userNick = nick;
    }

    public String getUserIcon(){
        return userIcon;
    }

    public void setUserIcon(String url){
        this.userIcon = url;
    }

    public String getRefName(){
        return refName;
    }

    public String getTitle(){
        return title;
    }

    public List<VoteOption> getLtVoteOption(){
        return ltVoteOption;
    }

    public int getOptionType(){
        return optionType;
    }

    public int getVoteType(){
        return voteType;
    }

    public int getUserType(){
        return userType;
    }

    public long getPlayTime(){
        return playTime;
    }

    public String getStartTime(){
        return startTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public String getPlatform(){
        return platform;
    }

    public long getCreateTime(){
        return createTime;
    }

    public long getUpdateTime(){
        return updateTime;
    }

    public boolean getDelFlag(){
        return delFlag;
    }

    public int getAudit(){
        return audit;
    }

    public boolean getAuditFirst(){
        return auditFirst;
    }

    public String getFrom(){
        return from;
    }

    public String getVersion(){
        return version;
    }

    public String getIp(){
        return ip;
    }

    public String getPort(){
        return port;
    }

    public String getRule(){
        return rule;
    }

    public boolean getAward(){
        return award;
    }

    public static class Builder{
        private VoteInfoBean usrCheckVoteModel = new VoteInfoBean();

        public Builder setVid(int vid){
            usrCheckVoteModel.vid = vid;
            return this;
        }

        public Builder setUserName(String userName){
            usrCheckVoteModel.userName = userName;
            return this;
        }

        public Builder setRefName(String refName){
            usrCheckVoteModel.refName = refName;
            return this;
        }

        public Builder setTitle(String title){
            usrCheckVoteModel.title = title;
            return this;
        }

        public Builder setMapOption(Map<String,String> mapOption)
        {
            usrCheckVoteModel.mapOption = mapOption;
            return this;
        }

        public Builder setMapOptionCount(Map<String, Integer> mapOptionCount)
        {
            usrCheckVoteModel.mapOptionCount = mapOptionCount;
            return this;
        }

        public Builder setDel(boolean del)
        {
            usrCheckVoteModel.del = del;
            return this;
        }

        public Builder setValid(boolean valid)
        {
            usrCheckVoteModel.valid = valid;
            return this;
        }


        public Builder setLtVoteOption(List<VoteOption> ltVoteOption){
            usrCheckVoteModel.ltVoteOption = ltVoteOption;
            return this;
        }

        public Builder setOptionType(int optionType){
            usrCheckVoteModel.optionType = optionType;
            return this;
        }

        public Builder setVoteType(int voteType){
            usrCheckVoteModel.voteType = voteType;
            return this;
        }

        public Builder setUserType(int userType){
            usrCheckVoteModel.userType = userType;
            return this;
        }

        public Builder setPlayTime(long playTime){
            usrCheckVoteModel.playTime = playTime;
            return this;
        }

        public Builder setStartTime(String startTime){
            usrCheckVoteModel.startTime = startTime;
            return this;
        }

        public Builder setEndTime(String endTime){
            usrCheckVoteModel.endTime = endTime;
            return this;
        }

        public Builder setPlatform(String platform){
            usrCheckVoteModel.platform = platform;
            return this;
        }

        public Builder setTotalUserCount(int count)
        {
            usrCheckVoteModel.totalUserCount = count;
            return this;
        }

        public Builder setCreateTime(long createTime){
            usrCheckVoteModel.createTime = createTime;
            return this;
        }

        public Builder setUpdateTime(long updateTime){
            usrCheckVoteModel.updateTime = updateTime;
            return this;
        }

        public Builder setDelFlag(boolean delFlag){
            usrCheckVoteModel.delFlag = delFlag;
            return this;
        }

        public Builder setAudit(int audit){
            usrCheckVoteModel.audit = audit;
            return this;
        }

        public Builder setAuditFirst(boolean auditFirst){
            usrCheckVoteModel.auditFirst = auditFirst;
            return this;
        }

        public Builder setFrom(String from){
            usrCheckVoteModel.from = from;
            return this;
        }

        public Builder setVersion(String version){
            usrCheckVoteModel.version = version;
            return this;
        }

        public Builder setIp(String ip){
            usrCheckVoteModel.ip = ip;
            return this;
        }

        public Builder setPort(String port){
            usrCheckVoteModel.port = port;
            return this;
        }

        public Builder setRule(String rule){
            usrCheckVoteModel.rule = rule;
            return this;
        }

        public Builder setAward(boolean award){
            usrCheckVoteModel.award = award;
            return this;
        }

        public VoteInfoBean getUsrVote()
        {
            return usrCheckVoteModel;
        }
    }

    public static final class VoteOption
    {
        private int vid;
        private int optionId;
        private String option;

        public int getVid() {
            return vid;
        }

        public int getOptionId() {
            return optionId;
        }

        public String getOption() {
            return option;
        }
    }

    public static Map<String,VoteInfoBean> getMapVoteInfo(JSONObject data)
    {
        JSONObject jsonObject = data.optJSONObject("mapVoteInfo");
        if (jsonObject == null) return null;
        final Iterator<String> keys = jsonObject.keys();
        final Map<String,VoteInfoBean> result = Maps.newHashMap();
        while (keys.hasNext())
        {
            final String key = keys.next();
            result.put(key,getVoteInfoBean(jsonObject.optJSONObject(key)));
        }

        return result;
    }

    public static VoteInfoBean getVoteInfoBean(JSONObject jsonObj)
    {
        if (jsonObj == null) return null;
        return new VoteInfoBean.Builder()
                .setVid(jsonObj.optInt("vid"))
                .setUserName(jsonObj.optString("userName"))
                .setRefName(jsonObj.optString("refName"))
                .setTitle(jsonObj.optString("title"))
                .setMapOption(getMapOption(jsonObj))
                .setLtVoteOption(getLtVoteOption(jsonObj))
                .setOptionType(jsonObj.optInt("optionType"))
                .setVoteType(jsonObj.optInt("voteType"))
                .setUserType(jsonObj.optInt("userType"))
                .setPlayTime(jsonObj.optLong("playTime"))
                .setStartTime(jsonObj.optString("startTime"))
                .setEndTime(jsonObj.optString("endTime"))
                .setPlatform(jsonObj.optString("platform"))
                .setTotalUserCount(jsonObj.optInt("totalUserCount"))
                .setCreateTime(jsonObj.optLong("createTime"))
                .setUpdateTime(jsonObj.optLong("updateTime"))
                .setDelFlag(jsonObj.optBoolean("delFlag"))
                .setAudit(jsonObj.optInt("audit"))
                .setAuditFirst(jsonObj.optBoolean("auditFirst"))
                .setFrom(jsonObj.optString("from"))
                .setVersion(jsonObj.optString("version"))
                .setIp(jsonObj.optString("ip"))
                .setPort(jsonObj.optString("port"))
                .setRule(jsonObj.optString("rule"))
                .setMapOptionCount(getMapOptionCount(jsonObj))
                .setDel(jsonObj.optBoolean("del"))
                .setValid(jsonObj.optBoolean("valid"))
                .setAward(jsonObj.optBoolean("award")).getUsrVote();
    }

    private static Map<String,String> getMapOption(JSONObject jsonObject)
    {
        final JSONObject mapOptionObj = jsonObject.optJSONObject("mapOption");
        if (mapOptionObj == null) return null;
        final Iterator<String> iter = mapOptionObj.keys();
        final Map<String,String> result = Maps.newHashMap();
        while (iter.hasNext())
        {
            final String key = iter.next();
            final String value = mapOptionObj.optString(key);
            result.put(key,value);
        }
        return result;
    }

    private static Map<String,Integer> getMapOptionCount(JSONObject jsonObject)
    {
        final JSONObject mapOptionObj = jsonObject.optJSONObject("mapOptionCount");
        if (mapOptionObj == null) return null;
        final Iterator<String> iter = mapOptionObj.keys();
        final Map<String,Integer> result = Maps.newHashMap();
        while (iter.hasNext())
        {
            final String key = iter.next();
            final int value = mapOptionObj.optInt(key);
            result.put(key,value);
        }
        return result;
    }
    
    private static List<VoteInfoBean.VoteOption> getLtVoteOption(JSONObject jsonObj){
        JSONArray jsonArray = jsonObj.optJSONArray("ltVoteOption");
        if(jsonArray == null) return null;
        final int len = jsonArray.length();
        List<VoteInfoBean.VoteOption> result = new ArrayList<VoteOption>();
        for(int i = 0; i < len; i++){
            JSONObject innerJSONObj = jsonArray.optJSONObject(i);
            result.add(getVoteOption(innerJSONObj));
        }
        return result;
    }

    private static VoteInfoBean.VoteOption getVoteOption(JSONObject jsonObj){
        final VoteInfoBean.VoteOption option = new VoteInfoBean.VoteOption();
        option.vid = jsonObj.optInt("vid");
        option.option = jsonObj.optString("option");
        option.optionId = jsonObj.optInt("optionId");
        return option;
    }
}

package example;

import http.HttpGetString;
import http.listener.HttpResultAction;
import http.listener.HttpResultListener;
import utils.LogUtils;
import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import example.delete.UsrDelVote;
import example.get.UsrCheckVotes;
import example.get.UsrCheckVotesModel;
import example.get.VoteInfoBean;
import example.post.UsrSendVote;
import example.put.PostNewStateV2;

/**
 * User: youxueliu Date: 14-2-28 Time: 下午1:34
 */
public class TestActivity extends Activity {
	private Context mContext;

	private static final String FEEDID = "88381696";
	private static final String[] FEEDIDS = { "88381696", "89997568",
			"89993920", "89977472", "89986880", "89966784" };
	private static final String REFNAME = "Movie_1008835";
	private static final String VOTE_ID = "1238902";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.vote_create_layout);
		mContext = getApplicationContext();

		// -------------test http get--------------------
		testUsrCheckVote();

		// -------------test http post-------------------
		testUsrSendVote();

		// -------------test http put--------------------
		testPostNewState();

		// -------------test http del--------------------
		testDelVote();
	}

	// -----------------用户查看投票(调通)------------------
	private void testUsrCheckVote() {
		final String[] vids = { "1600684" };
		new UsrCheckVotes(mContext, vids).get(UsrCheckVotesModel.class,
				new HttpResultAction<UsrCheckVotesModel>() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onSuccess(UsrCheckVotesModel result) {
						System.out.println(result);
					}

					@Override
					public void onFail(Throwable thr) {
						super.onFail(thr);
					}
				});
	}

	// ---------用户发起投票(调通)-------------------
	private void testUsrSendVote() {
		final UsrSendVote.Params params = new UsrSendVote.Params();
		final String[] options = { "这男的好傻", "这女的好厉害" };
		params.setRefName("Movie_1008835").setUsrName("lyx2007825")
				.setTitle("选哪个好呢？")
				.setVoteType(VoteInfoBean.VOTE_TYPE_MULTIPLE).setToken("token")
				.setOptions(options);
		new UsrSendVote(mContext, params)
				.send(new HttpResultListener<String>() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onSuccess(String result) {
						// 返回值:{"data":"901","err":0,"msg":null},data表示新生成的投票id,0表示成功
					}

					@Override
					public void onFail(Throwable thr) {
					}

				});
	}

	// 发动态
	private void testPostNewState() {
		final PostNewStateV2.Params params = new PostNewStateV2.Params();
		params.setToken("token").setContent("我来回复一下评论")
				.setRefId("Movie_1008835").setReplyId("108868")
				.setType(PostNewStateV2.Params.Type.COMMENT);
		new PostNewStateV2(mContext, params)
				.put(new HttpResultListener<String>() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onSuccess(String result) {
						// {"data":"88381696","err":0,"msg":null}
						// "88381696" is new commentId
					}

					@Override
					public void onFail(Throwable thr) {

					}
				});
	}

	// ---------------用户删除投票(调通)-----------------
	@SuppressWarnings("serial")
	private void testDelVote() {
		final String[] vids = { "901", "1001" };
		new UsrDelVote(mContext, "login token", vids)
				.delete(new HttpResultListener<String>() {
					@Override
					public void onSuccess(String result) {
						// 返回数据: {"data":"success","err":0,"msg":null}
					}

					@Override
					public void onFail(Throwable thr) {
					}
				});
	}


}

package com.m520it.www.newsreader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.adapter.CommentAdapter;
import com.m520it.www.newsreader.bean.CommentBean;
import com.m520it.www.newsreader.bean.CommentComparator;
import com.m520it.www.newsreader.http.HttpHelper;
import com.m520it.www.newsreader.http.HttpStringCallBack;
import com.m520it.www.newsreader.util.Constant;
import com.m520it.www.newsreader.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static com.m520it.www.newsreader.activity.NewsDetailActivity.DOC_ID;

public class KeybroadManActivity extends AppCompatActivity {


    private String mDocid;
    private ListView mListView_reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keybroad_man);
        Intent intent = getIntent();
        mDocid = intent.getStringExtra(DOC_ID);
        initView();
        initData();
    }

    private void initData() {
        requestData();
    }

    private void requestData() {
        String url = Constant.getNewsReplyUrl(mDocid);
        Log.e("xmg", "requestData: url "+url);
        HttpHelper.getInstance(getApplicationContext()).requestGETStringResult(url, new HttpStringCallBack() {
            @Override
            public void onSuccessResponse(String result) {
                Log.e("xmg", "onSuccessResponse: "+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    //先手动解析上面的数组commentIds,得到一个个关于评论JsonObject的key(id)
                    JSONArray commentIds = jsonObject.optJSONArray("commentIds");
                    JSONObject comments = jsonObject.optJSONObject("comments");
                    //开始遍历,取出一个个id
                    int length = commentIds.length();
                    //存放所有评论数据的集合
                    ArrayList<CommentBean> commentList = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        String id = commentIds.getString(i);
                        //id可能是楼中楼的形式,需要做处理,只拿最后一个id即可
                        if(id.contains(",")){
                            int start = id.lastIndexOf(",");
                            id = id.substring(start + 1);
                        }
                        //拿到一个id,就开始在下面的那个大的JsonObject(comments)中,去取出一个个评论JsonObject出来
                        JSONObject comment = comments.optJSONObject(id);
                        //取出来以后,就可以使用GSON了,因为评论JsonObject里面的字段都一样
                        CommentBean commentBean = JsonUtil.parseJson(comment.toString(), CommentBean.class);
                        commentList.add(commentBean);
                        Log.e("xmg", "onSuccessResponse: "+id+" commentBean "+commentBean.getVote());
                    }
                    //应该在循环外面去设置listVIew数据
                    initListViewData(commentList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private void initListViewData(ArrayList<CommentBean> commentList) {
        //使用冒泡排序
        //工具类
        Collections.sort(commentList,new CommentComparator());

        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        mListView_reply.setAdapter(commentAdapter);
    }

    private void initView() {
        mListView_reply = (ListView) findViewById(R.id.listView_reply);
    }
}

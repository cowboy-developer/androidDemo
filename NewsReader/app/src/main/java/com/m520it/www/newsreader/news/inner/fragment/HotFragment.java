package com.m520it.www.newsreader.news.inner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.activity.NewsDetailActivity;
import com.m520it.www.newsreader.http.HttpHelper;
import com.m520it.www.newsreader.http.HttpStringCallBack;
import com.m520it.www.newsreader.news.inner.adapter.HotNewsListAdapter;
import com.m520it.www.newsreader.news.inner.bean.BannerBean;
import com.m520it.www.newsreader.news.inner.bean.HotNewsBean;
import com.m520it.www.newsreader.news.inner.bean.NewsDetailBean;
import com.m520it.www.newsreader.util.Constant;
import com.m520it.www.newsreader.util.JsonUtil;
import com.m520it.www.newsreader.widget.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.m520it.www.newsreader.activity.NewsDetailActivity.DOC_ID;

/**
 * Created by xmg on 2017/1/2.
 */

//这个是新闻NewsFragment中的内部Fragment,展示的是头条新闻
public class HotFragment extends Fragment {
    private View mInflate;
    private ListView mListView_hot;
    private HotNewsListAdapter mNewsAdapter;
    private PtrClassicFrameLayout mPtr_frame;
    private BannerView mBannerView;

//    private OkHttpClient mClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        TextView textView = new TextView(getContext());
//        textView.setText("头条热门新闻");
//        textView.setTextSize(40);
//        textView.setTextColor(Color.RED);
        if(mInflate==null){
            mInflate = inflater.inflate(R.layout.frag_hot, container, false);
            initView();
        }
        return mInflate;
    }

    private void initView() {
        mListView_hot = (ListView) mInflate.findViewById(R.id.listView_hot);
        mPtr_frame = (PtrClassicFrameLayout) mInflate.findViewById(R.id.ptr_frame);
        //设置下拉刷新的回调监听
        mPtr_frame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //加载网络  加载网络后(在回调中),让刷新完成mPtr_frame.refreshComplete();
                requestData(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        //初始化ListView的脚布局
        initFootView();

        LoadMoreScrollChangeListener changeListener = new LoadMoreScrollChangeListener();
        mListView_hot.setOnScrollListener(changeListener);
        mListView_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsDetailBean item = (NewsDetailBean) mListView_hot.getAdapter().getItem(position);
                String docid = item.getDocid();
                Intent intent = new Intent();
                intent.putExtra(DOC_ID,docid);
                intent.setClass(getContext(), NewsDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initFootView() {
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_more, null);
        mListView_hot.addFooterView(footView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mClient = new OkHttpClient();
        initData();
    }

    private void initData() {
        requestData(false);

//        //准备一个请求
//        Request request = new Request.Builder()
//                .url(Constant.HOT_NEWS_JSON_URL)
//                .build();
//        mClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("xmg", "onFailure: "+"请求失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                //json字符串
//                String string = response.body().string();
//                LogUtil.e("xmg",string);
//            }
//        });
    }

    private int loadMoreCount = 0;//加载更多的次数
    /**
     * 请求数据
     * @param isLoadMore    是否为加载更多
     */
    private void requestData(final boolean isLoadMore) {
        String url;
        if(!isLoadMore){
            url = Constant.getHotUrl(0,9);
        }else{
            loadMoreCount++;
            //加载更多
            url = Constant.getHotUrl(0+loadMoreCount*10,9+loadMoreCount*10);
        }
        HttpHelper.getInstance(getContext()).requestGETStringResult(url,
                new HttpStringCallBack() {
                    @Override
                    public void onSuccessResponse(String result) {
                        setRefreshFinish();
                        HotNewsBean hotNewsBean = JsonUtil.parseJson(result, HotNewsBean.class);
//                        LogUtil.e("xmg", hotNewsBean.toString());
//                        String name = Thread.currentThread().getName();
//                        Log.e("xmg", "onSuccessResponse: Thread "+name);
                        setDataToListView(hotNewsBean,isLoadMore);
                    }

                    @Override
                    public void onFail(Exception e) {
                        Log.e("xmg", "onFailure: " + "请求失败");
                        setRefreshFinish();
                    }
                });
    }

    private void setRefreshFinish(){
        if(mPtr_frame!=null&&mPtr_frame.isRefreshing()){
            //让它刷新完成
            mPtr_frame.refreshComplete();
        }
    }

    private void setDataToListView(HotNewsBean hotNewsBean, boolean isLoadMore) {
        //加载更多时,不应该设置轮播图    不应该重新设置Adapter,应该在以前的Adapter的基础上添加数据

        //给ListView生成Adapter,设置数据
        List<NewsDetailBean> t1348647909107 = hotNewsBean.getT1348647909107();
        if(!isLoadMore){
            //不是加载更多,需要准备轮播图数据
            //第一条是轮播图数据.将其移除出来
            NewsDetailBean banner = t1348647909107.remove(0);
            //添加一个轮播图效果
            setBanner(banner);
        }
        //先做轮播图下面的ListView来展示新闻了
        if(mNewsAdapter==null){
            mNewsAdapter = new HotNewsListAdapter(t1348647909107);
            mListView_hot.setAdapter(mNewsAdapter);
        }else{
            //对数据Adapter做更新
//            mNewsAdapter.updateData(t1348647909107);
            if(!isLoadMore){
                //下拉刷新或首次加载都会走到这里来
                mNewsAdapter.updateData(t1348647909107,true);
            }else{
                //加载更多
                mNewsAdapter.updateData(t1348647909107,false);
            }
        }



    }

    //设置轮播图
    private void setBanner(NewsDetailBean banner) {
//        TextView textView = new TextView(getContext());
//        textView.setText("我不是轮播图");
//        textView.setTextSize(45);
//        textView.setTextColor(Color.RED);
        List<BannerBean> ads = banner.getAds();
        //准备图片地址集合和标题文本数据集合
        ArrayList<String> bannerTitles = new ArrayList<>();
        ArrayList<String> bannerImgUrls = new ArrayList<>();
        int size = ads.size();
        for (int i = 0; i < size; i++) {
            BannerBean bannerBean = ads.get(i);
            String imgsrc = bannerBean.getImgsrc();
            String title = bannerBean.getTitle();
            bannerImgUrls.add(imgsrc);
            bannerTitles.add(title);
        }

        //如果是刷新,不用每次都去添加
        //判断bannerView是否null,为Null,首次进来加载轮播图
        if(mBannerView==null){
            mBannerView = new BannerView(getContext(),bannerTitles,bannerImgUrls);
            mListView_hot.addHeaderView(mBannerView);
        }else{
            //否则就直接调用控件的刷新方法
            mBannerView.updateData(bannerImgUrls,bannerTitles);
        }

        //开始轮播
//        bannerView.startLoop();
//        bannerView.startLoop();
    }

    private class LoadMoreScrollChangeListener implements AbsListView.OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当滚动停止时 如果展示了ListView此时的展示出来的最后一条数据正好是最后一个item
            //加载更多
            if(scrollState==SCROLL_STATE_IDLE){
                int lastVisiblePosition = view.getLastVisiblePosition();
                int lastItemPosition = view.getAdapter().getCount() - 1;
//                Log.e("xmg", "onScrollStateChanged: lastItemPosition"+lastItemPosition);
                if(lastVisiblePosition==lastItemPosition){
                    //加载更多
//                    Log.e("xmg", "onScrollStateChanged: "+"加载更多");
                    requestData(true);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
}

package com.m520it.www.newsreader.news.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.m520it.www.newsreader.MainActivity;
import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.event.ShowTabHostEvent;
import com.m520it.www.newsreader.news.adapter.NewsFragmentAdapter;
import com.m520it.www.newsreader.news.adapter.ShowTitleAdapter;
import com.m520it.www.newsreader.news.adapter.ToAddTitleAdapter;
import com.m520it.www.newsreader.news.inner.fragment.EmptyFragment;
import com.m520it.www.newsreader.news.inner.fragment.HotFragment;
import com.m520it.www.newsreader.util.SharedPreferenceUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xmg on 2017/1/2.
 */

public class NewsFragment extends Fragment{

    private View mInflate;
    private ViewPager mViewPager;
    private SmartTabLayout mSmartTabLayout;
    private ImageButton mBtnArrow;
    private ValueAnimator mValueAnimToUp;
    private ValueAnimator mValueAnimToDown;
    private FrameLayout mFl_change_title;
    private TranslateAnimation mTranslateAnimShow;
    private TranslateAnimation mTranslateAnimHide;
    private TextView mTv_change_tip;
    private GridView mGvShowTitle;
    private GridView mGvAddTitle;
    private TextView mTv_change_done;//那个完成按钮
    private ShowTitleAdapter mShowTitleAdapter;
    private ToAddTitleAdapter mToAddTitleAdapter;
    private NewsFragmentAdapter mNewsFragmentAdapter;//关于ViewPager中Fragment的Adapter
    private ArrayList<Fragment> mFragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        TextView textView = new TextView(getContext());
//        textView.setText("新闻");
//        textView.setTextSize(40);
//        textView.setTextColor(Color.RED);
        //因为FragmentStatePagerAdapter会保存状态,不必要每次都去new
            mInflate = inflater.inflate(R.layout.frag_news, container, false);
            initView();
        return mInflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设置关于后退键的监听回调
        MainActivity activity = (MainActivity) getActivity();
        activity.setOnBackPressListener(new MainActivity.OnBackPressListener() {
            @Override
            public boolean onBackPress() {
                //调用一下关闭页面的判断的方法
                boolean b = NewsFragment.this.onBackPress();
                return b ;
            }
        });
        initData();
    }

    private void initData() {

        //给切换标题的两个GridView设置Adapter
        String showTitleString = SharedPreferenceUtil.getString(getContext(),
                SharedPreferenceUtil.SHOW_TITLE_DATA);
        String toAddTitleString = SharedPreferenceUtil.getString(getContext(),
                SharedPreferenceUtil.TO_ADD_TITLE_DATA);
        List<String> showTitleList = stringFormatToList(showTitleString);
        List<String> toAddTitleList = stringFormatToList(toAddTitleString);
        if(showTitleList.size()==0){
            //第一个List的size就为0,说明压根没缓存.读取StringArray的数据
            String[] titles = getResources().getStringArray(R.array.news_fragment_titles);
            showTitleList.addAll(Arrays.asList(titles));
            toAddTitleList.addAll(Arrays.asList(getResources().getStringArray(R.array.news_fragment_to_add_titles)));
        }
        //给mLastTitle设置初始值
        mLastTitle = showTitleString;
        mShowTitleAdapter = new ShowTitleAdapter(showTitleList);
        mToAddTitleAdapter = new ToAddTitleAdapter(toAddTitleList);

        mGvAddTitle.setAdapter(mToAddTitleAdapter);
        mGvShowTitle.setAdapter(mShowTitleAdapter);


        mFragments = new ArrayList<>();

        for (int i = 0; i < showTitleList.size(); i++) {
            if(i==0){
                HotFragment hotFragment = new HotFragment();
                mFragments.add(hotFragment);
            }else{
                EmptyFragment emptyFragment = new EmptyFragment();
                mFragments.add(emptyFragment);
            }
        }

        mNewsFragmentAdapter = new NewsFragmentAdapter(getChildFragmentManager(),
                mFragments,showTitleList);

        mViewPager.setAdapter(mNewsFragmentAdapter);

        Log.e("xmg", "initData: "+(mSmartTabLayout==null));
        mSmartTabLayout.setViewPager(mViewPager);

    }

    //点击后退应该要执行的一些逻辑
    //返回值代表是否应该走关闭页面    true 就关当前页面,false就不关
    public boolean onBackPress(){
        //判断如果叉号在的话
        if(mShowTitleAdapter.isShowDelete()){
            //如果不关当前页面,后退键按下后,就相当于点击了完成
            mTv_change_done.performClick();
            return false;
        }
        //判断如果白屏的FrameLayout在的话,也不关
        if(mFl_change_title.getVisibility()==View.VISIBLE){
            //如果不关当前页面,后退键按下后,就相当于点击了箭头
            mBtnArrow.performClick();
            return false;
        }
        return true;
    }

    private void initView() {
        mViewPager = (ViewPager) mInflate.findViewById(R.id.viewPager);
        mFl_change_title = (FrameLayout) mInflate.findViewById(R.id.fl_change_title);
//        mSmartTabLayout = (SmartTabLayout) mInflate.findViewById(R.id.include_tab).findViewById(R.id.viewpagerTab);
        View include = mInflate.findViewById(R.id.include_tab);
        mSmartTabLayout = (SmartTabLayout) include.findViewById(R.id.viewpagerTab);
        mBtnArrow = (ImageButton) include.findViewById(R.id.ibtn_arrow);
        mTv_change_tip = (TextView) include.findViewById(R.id.tv_change_tip);
        mTv_change_done = (TextView) include.findViewById(R.id.tv_change_done);
        //初始化箭头的动画
        initArrowAnim();
        initChangeTitleAnim();
        //设置白屏FrameLayout的布局效果
        initChangeTitleView();
        ArrowClickListener arrowClickListener = new ArrowClickListener();
        mBtnArrow.setOnClickListener(arrowClickListener);
        //给完成按钮设置点击
        mTv_change_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowTitleAdapter.setShowDelete(false);
                mTv_change_done.setVisibility(View.GONE);
            }
        });
    }

    private void initChangeTitleView() {
        View inflate = View.inflate(getContext(),R.layout.view_change_title,null);
        mGvShowTitle = (GridView) inflate.findViewById(R.id.gv_show_title);
        mGvAddTitle = (GridView) inflate.findViewById(R.id.gv_add_title);
        //设置长按item事件
        mGvShowTitle.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //修改Adapter中isShowDelete,然后notify
                mShowTitleAdapter.setShowDelete(true);
                mTv_change_done.setVisibility(View.VISIBLE);
                //消费掉这个事件
                return true;
            }
        });
        //设置点击item事件
        mGvShowTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //如果是出现了叉号,开始做删除逻辑,否则就让ViewPager跳转到对应的页面
                if(mShowTitleAdapter.isShowDelete()){
                    if(position==0){
                        //头条不让删
                        return;
                    }
                    //从mShowTitleAdapter中删除
                    String deleteItem = mShowTitleAdapter.deleteItem(position);
                    //添加到mTOAddTitleAdapter中
                    mToAddTitleAdapter.addItem(deleteItem);
                }else{
                    //将白屏FrameLayout收起来,通过点击箭头按钮就可以收起来
                    //performClick  该方法可以模拟点击该按钮
                    mBtnArrow.performClick();
                    mViewPager.setCurrentItem(position);
                }
            }
        });
        mGvAddTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String deleteItem = mToAddTitleAdapter.deleteItem(position);
                mShowTitleAdapter.addItem(deleteItem);
            }
        });
        mFl_change_title.addView(inflate);
    }

    private void initChangeTitleAnim() {
        //这里使用补间动画中的位移
        //展示出来的向下的动画
        mTranslateAnimShow = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, -1,
                TranslateAnimation.RELATIVE_TO_SELF, 0);
        //向上的隐藏动画
        mTranslateAnimHide = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, -1);
        mTranslateAnimShow.setDuration(500);
        mTranslateAnimHide.setDuration(500);
    }

    private void initArrowAnim() {
        ArrowAnimatorUpdateListener updateListener = new ArrowAnimatorUpdateListener();
        ArrowAnimatorListener arrowAnimatorListener = new ArrowAnimatorListener();
        //让箭头向上
        mValueAnimToUp = ValueAnimator.ofFloat(0f, 180f).setDuration(500);
        mValueAnimToUp.addUpdateListener(updateListener);
        mValueAnimToUp.addListener(arrowAnimatorListener);
        //让箭头继续向下
        mValueAnimToDown = ValueAnimator.ofFloat(180f, 0f).setDuration(500);
        //需要让控件真正的动起来,需要给动画添加一个动画更新的监听器,
        // 在监听器中,根据动画当前的值,来真正的设置旋转角度
        mValueAnimToDown.addUpdateListener(updateListener);
        mValueAnimToDown.addListener(arrowAnimatorListener);
    }

    private boolean mIsArrowDown = true;//箭头按钮是否向下
    private boolean mIsAnimFinish = true;//认为动画是否已经播放完毕

    private class ArrowClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //提前判断下动画是否结束,没结束就return
            if(!mIsAnimFinish){
                return;
            }
//            MainActivity activity = (MainActivity) getActivity();
            if(mIsArrowDown){
                //箭头向下
                mValueAnimToUp.start();
                //让下面的白屏FrameLayout展示并播放动画
                mFl_change_title.setVisibility(View.VISIBLE);
                mFl_change_title.startAnimation(mTranslateAnimShow);
                mTv_change_tip.setVisibility(View.VISIBLE);
                //让MainActivity去隐藏下面的tabHost
//                activity.showTabHost(false);
                EventBus.getDefault().post(new ShowTabHostEvent(false));
            }else{
                mValueAnimToDown.start();
                mFl_change_title.startAnimation(mTranslateAnimHide);
                mTv_change_tip.setVisibility(View.GONE);
                //让MainActivity去展示下面的tabHost
//                activity.showTabHost(true);
                EventBus.getDefault().post(new ShowTabHostEvent(true));
                //将数据做持久化保存
                saveCache();

                String newTitle = listFormatToString(mShowTitleAdapter.getData());
                if(mLastTitle.equals(newTitle)){
                    Log.e("xmg", "onClick: "+"数据相等,不再刷新");
                    //为了避免来不及取反,就在这里再加一句
                    mIsArrowDown = !mIsArrowDown;
                    return;
                }
                mLastTitle = newTitle;
                //还应该要刷新Fragment的展示数据
                refreshFragmentAdapter();
            }
            //点击以后就取反
            mIsArrowDown = !mIsArrowDown;
        }
    }

    //代表是先前的标题数据,
    // 刚进来的初始值就是从缓存中读出来的初始值
    //后面点击按钮收起面板时,将最新的标题数据赋值给mLastTitle
    private String mLastTitle = "";

    //将Fragment中展示进行刷新
    private void refreshFragmentAdapter() {
        //调用Adapter的更新方法
        mFragments.clear();
        //上面那个GridView的数据适配器
        List<String> data = mShowTitleAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if(i==0){
                HotFragment hotFragment = new HotFragment();
                mFragments.add(hotFragment);
            }else{
                EmptyFragment emptyFragment = new EmptyFragment();
                mFragments.add(emptyFragment);
            }
        }
        //ViewPager刷新了
        mNewsFragmentAdapter.update(mFragments,data);
        //SmartTabLayout刷新一下
        mSmartTabLayout.setViewPager(mViewPager);
    }

    //对切换栏目当中两个GridView数据做保存
    private void saveCache() {
        List<String> showTitleData = mShowTitleAdapter.getData();
        SharedPreferenceUtil.putString(getContext(),
                SharedPreferenceUtil.SHOW_TITLE_DATA,listFormatToString(showTitleData));
        List<String> toAddTitleData = mToAddTitleAdapter.getData();
        SharedPreferenceUtil.putString(getContext(),
                SharedPreferenceUtil.TO_ADD_TITLE_DATA,listFormatToString(toAddTitleData));
    }

    //来一个方法将List<String>转为一个整体String,方便保存
    private String listFormatToString(List<String> stringList){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            String s =  stringList.get(i);
            stringBuilder.append(s);
            //最后一个不加分割线了
            if(i!=stringList.size()-1){
                stringBuilder.append("_");
            }
        }
        return stringBuilder.toString();
    }

    private List<String> stringFormatToList(String cache){
        String[] split = cache.split("_");
        //防止异常报错
        //有可能缓存里就一个title,但是缓存为空,也是一个title,区分:第一个String为空的话,肯定是没缓存
        if(split==null|| TextUtils.isEmpty(split[0])){
            return new ArrayList<>();
        }
        ArrayList<String> strings = new ArrayList<>();
        //不要直接赋值返回,会报错的,最好采用addAll来添加
        strings.addAll(Arrays.asList(split));
        return strings;
    }


    private class ArrowAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener{

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float animatedValue = (float) animation.getAnimatedValue();
            //根据这个值来修改角度
            mBtnArrow.setRotation(animatedValue);
        }
    }

    private class ArrowAnimatorListener implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animation) {
            mIsAnimFinish = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mIsAnimFinish = true;
            if(mIsArrowDown){
                //播放完以后,箭头是向下的,我就隐藏那个白屏FrameLayout
                mFl_change_title.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}

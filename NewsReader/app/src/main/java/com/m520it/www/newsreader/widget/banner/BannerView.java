package com.m520it.www.newsreader.widget.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m520it.www.newsreader.R;
import com.m520it.www.newsreader.util.ImageUtil;

import java.util.ArrayList;

/**
 * Created by xmg on 2017/1/3.
 */
//轮播图相关的一个自定义控件
//系统的控件的基础上加了点自己的业务逻辑,完全没用到自定义控件的三大方法measure layout draw
//如果要把layout文件的效果打气到当前自定义控件上,
// 那么,layout文件中的父布局不要与自定义控件的父类冲突
public class BannerView extends RelativeLayout {

    private ArrayList<String> mTitles;
    private ArrayList<String> mImgUrls;
    private ViewPager mViewPager;
    private TextView mTextView;
    private LinearLayout mLl_dot;
    private  int mPicSize;

    private final int LOOP_TIME = 2000;//每张图展示的时间
    private Handler mHandler;
    private BannerAdapter mBannerAdapter;


    public BannerView(Context context, ArrayList<String> bannerTitles, ArrayList<String> bannerImgUrls) {
        super(context);
        //需要标题内容String  图片地址String
        this.mTitles = bannerTitles;
        this.mImgUrls = bannerImgUrls;
        mPicSize = mImgUrls.size();
        initView();
        initData();
    }

    private void initData() {
        initHandler();

        initUnLimitPic();

        //换成传递ImageView的集合
        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < mImgUrls.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        position = position%mImgUrls.size();
            ImageUtil.getInstance().display(mImgUrls.get(i),imageView);
            imageViews.add(imageView);
        }
//        BannerAdapter bannerAdapter = new BannerAdapter(mImgUrls);
        BannerAdapter bannerAdapter = new BannerAdapter(imageViews);
        mViewPager.setAdapter(bannerAdapter);

        //初始化小点出来
        initDot();

        //设置ViewPager的页面切换监听器
        BannerPagerChangeListener changeListener = new BannerPagerChangeListener();
        mViewPager.addOnPageChangeListener(changeListener);

        //默认指向第二张   (相当于原来的图集的第一张图)
        mViewPager.setCurrentItem(1);
        // 假设Integer.MAX_VALUE/2 == 13
        //
        // size =3
        // 13%3 == 1
//        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % mPicSize));
        //先让控件默认展示第一页的效果
//        mTextView.setText(mTitles.get(0));
//        changeDot(0);
    }

    private void initUnLimitPic() {
        //往图片集合当中首尾各添加一张图片来准备做无线循环(偷天换日)
        // 0 1 2 3 4  原来的
        //4 0 1 2 3 4 0  新的


        ArrayList<String> backup = new ArrayList<>();
        backup.addAll(mImgUrls);
        mImgUrls.clear();
        //添加最后一张到最前面来
        mImgUrls.add(backup.get(mPicSize-1));
        //添加中间的
        mImgUrls.addAll(backup);
        //添末加第一张图片到尾
        mImgUrls.add(backup.get(0));

    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //翻一次页面
                int currentItem = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(currentItem + 1);
                mHandler.sendEmptyMessageDelayed(0, LOOP_TIME);
            }
        };
    }

    private void initDot() {
        //先移除以前的小点
        mLl_dot.removeAllViews();
        for (int i = 0; i < mPicSize; i++) {
            ImageView imageView = new ImageView(getContext());
            //给控件设置背景形状
            imageView.setImageResource(R.drawable.bg_dot);
            //通过LayoutParams可以设置添加控件,更多的布局效果,比如:尺寸大小/margin/居中
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, -2);
            layoutParams.setMargins(0, 0, 10, 0);
            mLl_dot.addView(imageView, layoutParams);
        }
    }

    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_banner, this);
        mViewPager = (ViewPager) inflate.findViewById(R.id.viewPager_banner);
        mTextView = (TextView) inflate.findViewById(R.id.tv_banner_title);
        mLl_dot = (LinearLayout) inflate.findViewById(R.id.ll_dot);

        //只给ViewPager来写触摸事件
//        mViewPager.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int eventAction = event.getAction();
//                switch (eventAction) {
//                    case MotionEvent.ACTION_DOWN:
//                        stopLoop();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        startLoop();
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        //当触摸事件只有down,后续的事件没了,就会走到这里
//                        startLoop();
//                        break;
//                }
//                return true;
//            }
//        });

//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            int eventAction = event.getAction();
//            switch (eventAction) {
//                case MotionEvent.ACTION_DOWN:
//                    stopLoop();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    startLoop();
//                    break;
//            }
//            return true;
//        }
    }

//姿势:如果你希望在原先的触摸的基础上多一些逻辑,就开源考虑将该逻辑写到分发中dispatchTouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int eventAction = ev.getAction();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
//                stopLoop();
                break;
            case MotionEvent.ACTION_UP:
//                startLoop();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

//    @Override
//        public boolean dispBoolean(MotionEvent event) {
//            int eventAction = event.getAction();
//            switch (eventAction) {
//                case MotionEvent.ACTION_DOWN:
//                    stopLoop();
//                    break;
//                case MotionEvent.ACTION_UP:
//                    startLoop();
//                    break;
//            }
//            return true;
//        }


    //开始让轮播图动起来
    public void startLoop() {
        //确保轮播任务只有一个在进行
        stopLoop();
        mHandler.sendEmptyMessageDelayed(0, LOOP_TIME);
    }

    public void stopLoop() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public void updateData(ArrayList<String> bannerImgUrls, ArrayList<String> bannerTitles) {
        //todo 如果传过来的数据跟以前一样    直接return 

        //先把以前的集合数据做个更新
        mTitles.clear();
        mTitles.addAll(bannerTitles);
        mImgUrls.clear();
        mImgUrls.addAll(bannerImgUrls);
        mPicSize = mImgUrls.size();

        initUnLimitPic();
        //换成传递ImageView的集合
        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < mImgUrls.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        position = position%mImgUrls.size();
            ImageUtil.getInstance().display(mImgUrls.get(i),imageView);
            imageViews.add(imageView);
        }
//        BannerAdapter bannerAdapter = new BannerAdapter(mImgUrls);
        if(mBannerAdapter==null){
            mBannerAdapter = new BannerAdapter(imageViews);
            mViewPager.setAdapter(mBannerAdapter);
        }else{
            mBannerAdapter.updateData(imageViews);
        }
        //初始化小点出来
        initDot();
        //默认指向第二张   (相当于原来的图集的第一张图)
        mViewPager.setCurrentItem(1);

        //手动选中小点
        changeDot(1);
    }

    private class BannerPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // 0 1 2 3 4  原来的
            //4 0 1 2 3 4 0  新的
            position = getRealPosition(position);
            mTextView.setText(mTitles.get(position));
            changeDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //当状态为停下来时,不滚动时,再偷天换日
            if(state==ViewPager.SCROLL_STATE_IDLE){
                int currentItem = mViewPager.getCurrentItem();
                if(currentItem==0){
                    //应该就指向倒数第二张图
                    //第二个参数为false,没有切换的动画效果
                    mViewPager.setCurrentItem(mImgUrls.size()-1-1,false);
                }else if(currentItem==mImgUrls.size()-1){
                    //应该指向第二张图
                    mViewPager.setCurrentItem(1,false);
                }
            }

        }
    }

    private int getRealPosition(int position) {
        // 0 1 2 3 4  原来的
        //4 0 1 2 3 4 0  新的
        int realPosition = 0;
        if (position == 0) {
            realPosition = mPicSize-1;
        }else if(position == mImgUrls.size()-1){
            realPosition = 0;
        }else{
            //1  1-1
            //2  2-1
            //3  3-1
            realPosition = position-1;
        }
        return realPosition;
    }

    private void changeDot(int position) {
        //先把所有点的图片都改成正常的展示
        int childCount = mLl_dot.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView childAt = (ImageView) mLl_dot.getChildAt(i);
            if (i == position) {
                //这个位置是被选中的,将其背景设置为选中
                childAt.setImageResource(R.drawable.bg_dot_selected);
            } else {
                //这个位置是未被选中的,将其背景设置为正常
                childAt.setImageResource(R.drawable.bg_dot);
            }
        }
    }
}

package com.m520it.www.newsreader;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.m520it.www.newsreader.event.ShowTabHostEvent;
import com.m520it.www.newsreader.news.fragment.MeFragment;
import com.m520it.www.newsreader.news.fragment.NewsFragment;
import com.m520it.www.newsreader.news.fragment.TopicFragment;
import com.m520it.www.newsreader.news.fragment.VaFragment;
import com.m520it.www.newsreader.util.TitleUtil;
import com.m520it.www.newsreader.widget.MyFragmentTabHost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private MyFragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置状态栏变透明  SDK >=4.4 19
        if(Build.VERSION.SDK_INT>=19){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        EventBus.getDefault().register(MainActivity.this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MainActivity.this);
    }

    private void initData() {

    }

    private void initView() {
        if(Build.VERSION.SDK_INT>=19){
            ImageView iv_wrap_title = (ImageView) findViewById(R.id.iv_wrap_title);
            iv_wrap_title.getLayoutParams().height = TitleUtil.getStatusHeight(getApplicationContext());
            iv_wrap_title.setBackgroundColor(getResources().getColor(R.color.colorMain));
        }
        mTabHost = (MyFragmentTabHost) findViewById(R.id.tabHost);
        initTabHost();
    }


    private Class[] fragmentClasses = new Class[]{NewsFragment.class, VaFragment.class,
            TopicFragment.class, MeFragment.class};

    private int[] resIds = new int[]{R.drawable.bg_tab_news_selector,
            R.drawable.bg_tab_va_selector,
    R.drawable.bg_tab_topic_selector,
    R.drawable.bg_tab_me_selector};

    private String[] tabTexts;

    private void initTabHost() {

        //准备数组出来
        tabTexts = getResources().getStringArray(R.array.tab_titles);
        //1 初始化 它能够替换Fragment,也是使用了FragmentManager ,需要把FragmentManager传给它
        mTabHost.setup(getApplicationContext(),getSupportFragmentManager(),R.id.fl);
        //2 准备创建出来一个tab 不能直接用该类的方法来创建
        for (int i = 0; i < 4; i++) {
            TabHost.TabSpec tab = mTabHost.newTabSpec(String.valueOf(i));
            View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_tab, null);
            ImageView iv_item_tab = (ImageView) inflate.findViewById(R.id.iv_item_tab);
            TextView tv_item_tab = (TextView) inflate.findViewById(R.id.tv_item_tab);
            iv_item_tab.setImageResource(resIds[i]);
            tv_item_tab.setText(tabTexts[i]);
            tab.setIndicator(inflate);
            //3 添加一个个tab进来
            mTabHost.addTab(tab,fragmentClasses[i],null);
        }

        //设置初始时的选中某个特定的tab
//        mTabHost.setCurrentTab(1);
//        mTabHost.setCurrentTabByTag("d");
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Toast.makeText(MainActivity.this,"你点中的这个tab的id是:"+tabId,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        //应该去判断NewsFragment中一些标志位
        //通过回调,让Fragment中的某段代码在这里执行
        boolean isClose =true;
        if(mOnBackPressListener!=null){
            isClose  = mOnBackPressListener.onBackPress();
        }
        if(!isClose){
            //如果返回为false,就不关页面,直接return
            return;
        }
        super.onBackPressed();
    }


    private OnBackPressListener mOnBackPressListener;


    public void setOnBackPressListener(OnBackPressListener listener){
        this.mOnBackPressListener = listener;
    }

    public interface OnBackPressListener{
        boolean onBackPress();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showEvent(ShowTabHostEvent event){
        showTabHost(event.isShowTabHost());
    }


    public void showTabHost(boolean isShow){
        if(isShow){
            mTabHost.setVisibility(View.VISIBLE);
        }else{
            mTabHost.setVisibility(View.GONE);
        }
    }
}

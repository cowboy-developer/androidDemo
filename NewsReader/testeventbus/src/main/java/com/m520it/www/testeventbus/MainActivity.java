package com.m520it.www.testeventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

//import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private TextView mTv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EventBus
        //先注册才能接收得到
        EventBus.getDefault().register(MainActivity.this);
        mTv_info = (TextView) findViewById(R.id.tv_info);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MainActivity.this);
    }

    public void click(View view) {
        Intent intent = new Intent(getApplicationContext(), TwoActivity.class);
        startActivity(intent);
    }

    //来个方法接收
//    public void onEventMainThread(DriverEvent event){
//        mTv_info.setText(event.getInfo());
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acatadog(DriverEvent event){
        mTv_info.setText(event.getInfo());
    }

}

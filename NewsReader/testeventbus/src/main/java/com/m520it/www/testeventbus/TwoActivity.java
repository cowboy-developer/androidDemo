package com.m520it.www.testeventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

//import de.greenrobot.event.EventBus;

public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }

    public void driver(View view) {
        //在这里发车
        EventBus.getDefault().post(new DriverEvent("magnet:?xt=urn:btih:3f42b9d1dd6294c563ed96"));
    }
}

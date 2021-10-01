package com.m520it.www.skipview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SkipView mSkipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSkipView = (SkipView) findViewById(R.id.skipView);

        mSkipView.setOnSkipListener(new SkipView.OnSkipListener() {
            @Override
            public void onSkip() {
                Toast.makeText(MainActivity.this,"在MainActivity中跳转页面",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void click(View view) {
        mSkipView.startAutoSkip(4000);
        
    }
}

package com.m520it.www.newsreader.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by xmg on 2017/1/7.
 */

public class FitMeasureGridView extends GridView {
    public FitMeasureGridView(Context context) {
        super(context);
    }

    public FitMeasureGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //修改测量方法,让其正在高度即使处于UNSPECIFIED模式时,也能正常展示

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(heightMode==MeasureSpec.UNSPECIFIED){
            //>>右移2位  x>>y    代表值为x除以2的y次方,取整数
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

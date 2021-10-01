package com.m520it.www.skipview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xmg on 2016/12/31.
 */

public class SkipView extends View {

    String skipText = "跳过";
    int strokeWidth = 24;//外部圆弧的画笔的粗度
    int padding = 30;//内部圆的文字距离内部圆的边框的间距
    private float mInnerCircle;
    private float mOutCircle;
    private float mTextWidth;
    private RectF mRectF;
    private Paint mTextPaint;
    private Paint mInnerCirclePaint;
    private Paint mOutPaint;
    private Handler mHandler;

    public SkipView(Context context) {
        super(context);
    }

    public SkipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //文字相关的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(72);
        //内圆的画笔
        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setColor(Color.YELLOW);
        mInnerCirclePaint.setAntiAlias(true);
        //外部圆弧的画笔
        mOutPaint = new Paint();
        mOutPaint.setColor(Color.RED);
        mOutPaint.setAntiAlias(true);
        mOutPaint.setStyle(Paint.Style.STROKE);
//        mOutPaint.setStrokeWidth(1);
        mOutPaint.setStrokeWidth(strokeWidth);

        //计算内部圆的直径
            //先计算文字的宽度
        mTextWidth = mTextPaint.measureText(skipText);
        //n内部圆的直径
        mInnerCircle = mTextWidth + padding * 2;
        //计算外部圆弧所在圆的直径
        //内部圆的直径
        mOutCircle = mInnerCircle + strokeWidth*2;

        //准备一个矩形对象给圆弧,方便去绘制圆弧
        mRectF = new RectF(0+strokeWidth/2, 0+strokeWidth/2,
                mOutCircle-strokeWidth/2, mOutCircle-strokeWidth/2);

        //使用线程来不断绘制和更新时间
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    SystemClock.sleep(refreshTime);
//                    //计算当前的时间
//                    mCurrentTime = mCurrentTime+refreshTime;
//                    setDegree(5000,mCurrentTime);
//                }
//            }
//        }).start();
        //使用Handler一边发消息一边接消息, 接到消息handleMessage方法中,我继续给自己发消息,形成循环
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                //判断一下,如果时间走完了,就停止
                if(mCurrentTime>=mAutoSkipTime){
//                    Toast.makeText(getContext(),"我跳转页面了",Toast.LENGTH_SHORT).show();
                    if(mOnSkipListener!=null){
                        mOnSkipListener.onSkip();
                    }
                    Log.e("xmg", "handleMessage: "+"广告展示完毕");
                    return;
                }
                //接受到消息,就刷新角度,并重绘
                mCurrentTime +=refreshTime;
                setDegree(mCurrentTime);
                //继续给自己发消息
                mHandler.sendEmptyMessageDelayed(0,refreshTime);
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int)mOutCircle,(int)mOutCircle);
    }

    //外部调用,使用这个方法开始读秒,修改角度
    public void startAutoSkip(int autoSkipTime){
        mAutoSkipTime = autoSkipTime;
        //通过handler 发个消息开始进行循环
        mHandler.sendEmptyMessageDelayed(0,refreshTime);
    }

    int mDegree = 0;//代表圆弧要绘制的角度


    int mAutoSkipTime = 0;
    int mCurrentTime = 0;
    int refreshTime = 100;//为了看的流畅一些,设置刷新时间为100毫秒

    //不是传角度 ,根据时间来计算出当前应该要展示的角度
    //比如:自动跳过的时间为5000毫秒  已经看了2500毫秒时间的广告
    //后面会根据时间来决定是否继续绘制角度
    // (如果时间走完了,就应该停掉绘制,为了知道时间,来一个成员变量)
    public void setDegree(int currentTime){
//        mAutoSkipTime = autoSkipTime;
        mCurrentTime = currentTime;
        //计算角度
        float percent = (mCurrentTime * 1f) / mAutoSkipTime;
        mDegree = (int) (percent*360);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.5f);
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1f);
                //停掉Handler中的消息
//                mHandler.sendEmptyMessage()
                mHandler.removeCallbacksAndMessages(null);
                //跳转页面
                //写回调,插入一个接口的方法扔在这里,相当于...
                if(mOnSkipListener!=null){
                    mOnSkipListener.onSkip();
                }
//                Toast.makeText(getContext(),"我跳转页面了",Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制 从外画到里

        //绘制外部的圆弧
        //默认从正上方开始计算角度
        canvas.save();
        canvas.rotate(-90,mOutCircle/2,mOutCircle/2);
        canvas.drawArc(mRectF,0,mDegree,false,mOutPaint);
        canvas.restore();

        //绘制内部圆
        canvas.drawCircle(mOutCircle/2,mOutCircle/2,mInnerCircle/2,mInnerCirclePaint);
        //绘制文本
            //x y 代表文本内容的左下角,还要上来一点点
//        mTextWidth = mTextPaint.measureText(skipText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        //打印这四条关于font字体的基线
        float top = fontMetrics.top;
        float ascent = fontMetrics.ascent;
        float descent = fontMetrics.descent;
        float bottom = fontMetrics.bottom;
        Log.e("xmg", "onDraw: top"+top+" ascent "+ascent+"  descent "+descent
        +" bottom "+bottom);
        //计算出文本的高度的一半
        float textHalfHeight = (bottom - top) / 2;
        //先设置字体的baseLine,让字体的top线等于中心点
        int textLoctionY = (int) (mOutCircle/2+Math.abs(top));
        //再让文字向上走文字高度的一半
        textLoctionY = (int) (textLoctionY-textHalfHeight);
        canvas.drawText(skipText,mOutCircle/2-mTextWidth/2,textLoctionY,mTextPaint);
    }

    public void setOnSkipListener(OnSkipListener skipListener){
        this.mOnSkipListener = skipListener;
    }

    private OnSkipListener mOnSkipListener;

    public interface OnSkipListener{
        void onSkip();
    }
}

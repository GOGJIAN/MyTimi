package com.jianjian.android.mytimi.View;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jianjian.android.mytimi.R;
import com.jianjian.android.mytimi.model.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Lenovo on 2017/12/14.
 */

public class MainCycleView extends View {

    private static final String TAG = "drawTag";


    private float startPoint = 90;

    private float endPoint = 450;

    private float curPoint = 90;

    //动画时间
    private int animationLength = 1000;

    private float totalFee = 0;

    private Paint mPaint;

    private int cycleWidth = 10;

    List<Order> mOrders = new ArrayList<>();

    public MainCycleView(Context context) {
        super(context);
        init();
    }

    public MainCycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainCycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MainCycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        @SuppressLint("DrawAllocation")
        RectF rectF = new RectF(cycleWidth+5,cycleWidth+5,getWidth()-cycleWidth-5,getHeight()-cycleWidth-5);
        drawCenterCircle(canvas);
        drawInitMask(canvas,rectF);
        drawItem(canvas,rectF);
        drawMask(canvas,rectF);
        drawPlus(canvas);

    }

    private void drawCenterCircle(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        float centerX = getWidth()/2;
        canvas.drawCircle(centerX,centerX,getWidth()/2,paint);
    }

    private void drawPlus(Canvas canvas){
        mPaint.setColor(Color.BLUE);
        float centerX = getWidth()/2;
        RectF rectF = new RectF(centerX-40,centerX-40,centerX+40,centerX+40);
        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.ic_add)).getBitmap();
        canvas.drawBitmap(bitmap,null,rectF,null);
    }

    private void drawItem(Canvas canvas,RectF rectF){
        float curStartPoint = startPoint;
        float curSweepLength ;
        for(Order order :mOrders){
            mPaint.setColor(getResources().getColor(order.getColor()));
            curSweepLength = order.getMoney()/totalFee*360;
            canvas.drawArc(rectF,curStartPoint,curSweepLength,false,mPaint);
            curStartPoint+=curSweepLength;
        }
    }



    public void updateUI(){
        curPoint = 90;
        setAnimation(90,450,animationLength);
    }

    private void drawMask(Canvas canvas,RectF rectF){
        mPaint.setColor(Color.GRAY);
        Log.d(TAG, "drawMask: curPoint:"+curPoint);
        canvas.drawArc(rectF,curPoint,450-curPoint,false,mPaint);
    }
    private void drawInitMask(Canvas canvas,RectF rectF){
        mPaint.setColor(Color.GRAY);
        Log.d(TAG, "drawMask: curPoint:"+curPoint);
        canvas.drawArc(rectF,90,450,false,mPaint);
    }

    public void init(){
        mPaint = new Paint();
        mPaint.setStrokeWidth(cycleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        //setAnimation(90,450,animationLength);
    }

    public void setCycleWidth(int cycleWidth) {
        this.cycleWidth = cycleWidth;
    }

    public void setTotalFee(float fee){
        this.totalFee = fee;
    }


    private void setAnimation(float start, float end, int length){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start,end);
        valueAnimator.setTarget(curPoint);
        valueAnimator.setDuration(length);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                curPoint = (float)valueAnimator.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: curPoint:"+curPoint);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void setAnimationLength(int animationLength) {
        this.animationLength = animationLength;
    }

    public void setData(List<Order> orders){
        mOrders = new ArrayList<>(orders);
        Collections.sort(mOrders, new Comparator<Order>() {
            @Override
            public int compare(Order order, Order t1) {
                float i = order.getMoney();
                float j = t1.getMoney();
                if(i>j)
                    return -1;
                if(i<j)
                    return 1;
                return 0;
            }
        });
        setTotalFee();
    }
    private void setTotalFee(){
        float fee = 0f;
        for(Order o:mOrders){
            fee +=o.getMoney();
        }
        this.totalFee = fee;
    }




}

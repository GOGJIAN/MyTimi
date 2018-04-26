package com.jianjian.android.mytimi.View;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
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
import com.jianjian.android.mytimi.model.CycleItem;
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

    List<CycleItem> mItems = new ArrayList<>();

    public MainCycleView(Context context) {
        super(context);
        init(null);
    }

    public MainCycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MainCycleView);
        init(array);
        array.recycle();
    }

    public MainCycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MainCycleView);
        init(array);
        array.recycle();
    }

    public MainCycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.MainCycleView);
        init(array);
        array.recycle();
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
        for(CycleItem item:mItems){
            mPaint.setColor(getResources().getColor(item.getColor()));
            curSweepLength = item.getNum()/totalFee*360;
            canvas.drawArc(rectF,curStartPoint,curSweepLength,false,mPaint);
            curStartPoint+=curSweepLength;
        }
    }



    public void updateUI(){
        curPoint = 90;
        setAnimation(startPoint,endPoint,animationLength);
    }

    private void drawMask(Canvas canvas,RectF rectF){
        mPaint.setColor(Color.GRAY);
        Log.d(TAG, "drawMask: curPoint:"+curPoint);
        canvas.drawArc(rectF,curPoint,endPoint-curPoint,false,mPaint);
    }
    private void drawInitMask(Canvas canvas,RectF rectF){
        mPaint.setColor(Color.GRAY);
        Log.d(TAG, "drawMask: curPoint:"+curPoint);
        canvas.drawArc(rectF,startPoint,endPoint,false,mPaint);
    }

    public void init(@Nullable TypedArray array){
        if(array!=null){
            int cycleWidth = array.getInt(R.styleable.MainCycleView_cycleWidth,10);
            float startPoint = array.getFloat(R.styleable.MainCycleView_startPoint,90);
            float endPoint = array.getFloat(R.styleable.MainCycleView_endPoint,450);
            int animationLength = array.getInt(R.styleable.MainCycleView_animationLength,1000);

            this.cycleWidth = cycleWidth;
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.animationLength = animationLength;
        }
        mPaint = new Paint();
        mPaint.setStrokeWidth(cycleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
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



    public void setData(List<CycleItem> items){
        mItems = new ArrayList<>(items);
        Collections.sort(mItems, new Comparator<CycleItem>() {
            @Override
            public int compare(CycleItem order, CycleItem t1) {
                float i = order.getNum();
                float j = t1.getNum();
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
        for(CycleItem i:mItems){
            fee +=i.getNum();
        }
        this.totalFee = fee;
    }




}

package com.itheima.admin.gooview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.view.WindowManager.LayoutParams;

/**
 * Created by Administrator on 2016/9/27.
 */
public class GooView extends View{

    private Paint mPaint;
    private WindowManager mWindowmanager;

    public GooView(Context context) {
        this(context,null);
    }

    public GooView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GooView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);

        mWindowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    //Goo圆的控制点数组
    private PointF[] mGooControl = {new PointF(220f,385f),new PointF(220f,415f)};
    //Grag圆的控制点数组
    private PointF[] mDragControl = {new PointF(220f,385f),new PointF(220f,415f)};
    //控制点
    private PointF mControlPoint = new PointF(220f,400f);
    //两个圆的圆心
    private PointF mGooPoint = new PointF(220f,400f);
    private PointF mDragPoint = new PointF(220f,400f);

    private float mDragRadius = 15f;
    private float mGooRadius = 15f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);   //设置画笔描边
        canvas.drawCircle(mGooPoint.x,mGooPoint.y,maxDistance,mPaint);

        mPaint.setStyle(Paint.Style.FILL);  //画笔设置回填充

        //设置Goo的圆心随着拖拽逐渐变小
        float fraction = GeometryUtil.getDistanceBetween2Points(mDragPoint,mGooPoint) / maxDistance;
        mGooRadius = GeometryUtil.evaluateValue(fraction, 15f, 2f);


        canvas.drawCircle(mGooPoint.x,mGooPoint.y,mGooRadius,mPaint);
        canvas.drawCircle(mDragPoint.x,mDragPoint.y,mDragRadius,mPaint);

        if (!isOutBound) {      //不超出边界才画连线
            float offsetX = mGooPoint.x - mDragPoint.x;
            float offsetY = mGooPoint.y - mDragPoint.y;
            double link = offsetY / offsetX;
            if (offsetX != 0) {
                mGooControl = GeometryUtil.getIntersectionPoints(mGooPoint, mGooRadius, link);
                mDragControl = GeometryUtil.getIntersectionPoints(mDragPoint, mDragRadius, link);

                mControlPoint = GeometryUtil.getMiddlePoint(mGooPoint, mDragPoint);
            }

            Path path = new Path();
            //绘制起点
            path.moveTo(mGooControl[0].x,mGooControl[0].y);
            //参数为控制点和要到达的点
            path.quadTo(mControlPoint.x,mControlPoint.y,mDragControl[0].x,mDragControl[0].y);

            path.lineTo(mDragControl[1].x,mDragControl[1].y);
            path.quadTo(mControlPoint.x,mControlPoint.y,mGooControl[1].x,mGooControl[1].y);
            canvas.drawPath(path,mPaint);
        }

    }

    private float maxDistance = 100;
    private boolean isOutBound;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mDragPoint.set(event.getX(),event.getY());
                //判断是否超过边界
                float distanceBetween2Points = GeometryUtil.getDistanceBetween2Points(mDragPoint, mGooPoint);
                isOutBound = distanceBetween2Points > maxDistance;
                break;

            case MotionEvent.ACTION_UP:
                if (isOutBound) {
                    //超出边界播放爆炸动画
                    mDragRadius = 0;
                    playBoomAnimation();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDragPoint = new PointF(220f,400f);
                            mDragRadius = 15f;
                            invalidate();
                        }
                    },2000);

                }else {
                    //回弹
                    resetDragPoint();
                }
                break;

        }
        invalidate();
        return true;
    }

    private void playBoomAnimation() {
        LayoutParams params = new LayoutParams();
        params.format = PixelFormat.TRANSPARENT;//设置window的背景是透明的

        final BubbleLayout frameLayout = new BubbleLayout(getContext());
        ImageView imageView = getAnimImageView();
        //播放帧动画
        AnimationDrawable animDrawable = (AnimationDrawable) imageView.getBackground();
        animDrawable.start();
        frameLayout.addView(imageView);
        frameLayout.setBubblePosition(mDragPoint.x, mDragPoint.y + MainActivity.mHeight );
        //添加View
        mWindowmanager.addView(frameLayout, params);

        //动画播放完毕，将FrameLayout移除
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //移除
                mWindowmanager.removeView(frameLayout);
            }
        }, 601);
    }

    private ImageView getAnimImageView(){
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new FrameLayout.LayoutParams(68,68));
        imageView.setBackgroundResource(R.drawable.anim_bg);
        return imageView;
    }




    private void resetDragPoint() {
        final PointF currentPoint = new PointF(mDragPoint.x,mDragPoint.y);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                PointF pointByPercent = GeometryUtil.getPointByPercent(currentPoint, mGooPoint, animatedFraction);
                mDragPoint.set(pointByPercent);
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new OvershootInterpolator(4));
        valueAnimator.setDuration(360);
        valueAnimator.start();
    }
}

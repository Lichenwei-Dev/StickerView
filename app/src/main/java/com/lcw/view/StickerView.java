package com.lcw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义贴纸View
 * Create by: chenWei.li
 * Date: 2018/11/22
 * Time: 下午11:02
 * Email: lichenwei.me@foxmail.com
 */
public class StickerView extends View implements View.OnTouchListener {

    private Bitmap mImageBitmap;
    private Matrix mImageMatrix;
    private RectF mImageRectF;
    private float[] mImagePoints;

    private Paint mImageRectPaint;


    private float mLastX;
    private float mLastY;

    public StickerView(Context context) {
        super(context);
        initView(context);
    }

    public StickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //设置触摸监听
        setOnTouchListener(this);
        //初始化图片，矩阵等信息
        mImageBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
        mImageMatrix = new Matrix();
        mImageRectF = new RectF(0, 0, mImageBitmap.getWidth(), mImageBitmap.getHeight());
        mImagePoints = new float[]{
                0, 0,//左上
                mImageBitmap.getWidth(), 0,//右上
                mImageBitmap.getWidth(), mImageBitmap.getHeight(),//右下
                0, mImageBitmap.getHeight(),//左下
                mImageBitmap.getWidth() / 2, mImageBitmap.getHeight() / 2//中心点
        };

        //绘制图片外部矩形框
        mImageRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImageRectPaint.setColor(Color.BLACK);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mImageBitmap, mImageMatrix, null);
        float[] floats = getCurrentImagePoints();
        canvas.drawLine(floats[0], floats[1], floats[2], floats[3], mImageRectPaint);
        canvas.drawLine(floats[2], floats[3], floats[4], floats[5], mImageRectPaint);
        canvas.drawLine(floats[4], floats[5], floats[6], floats[7], mImageRectPaint);
        canvas.drawLine(floats[0], floats[1], floats[6], floats[7], mImageRectPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOnStickerView(mLastX, mLastY)) {//检测触摸点是否在贴纸上
                    float currentX = event.getX();
                    float currentY = event.getY();
                    mImageMatrix.postTranslate(currentX - mLastX, currentY - mLastY);
                    mLastX = currentX;
                    mLastY = currentY;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();
        return true;
    }


    /**
     * 检测当前触摸是否在贴纸上
     *
     * @param touchX
     * @param touchY
     * @return
     */
    private boolean isOnStickerView(float touchX, float touchY) {
        float[] dstPoints = new float[2];
        float[] srcPoints = new float[]{touchX, touchY};
        Matrix matrix = new Matrix();
        mImageMatrix.invert(matrix);
        matrix.mapPoints(dstPoints, srcPoints);
        return mImageRectF.contains(dstPoints[0], dstPoints[1]);
    }

    /**
     * 获取当前贴纸所对应的关键点
     *
     * @return
     */
    private float[] getCurrentImagePoints() {
        float[] dstPoints = new float[10];
        float[] srcPoints = mImagePoints;
        mImageMatrix.mapPoints(dstPoints, srcPoints);
        return dstPoints;
    }

}

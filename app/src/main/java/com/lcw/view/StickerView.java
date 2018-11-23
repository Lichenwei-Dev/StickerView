package com.lcw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
        setOnTouchListener(this);
        mImageBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
        mImageMatrix = new Matrix();
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
    }

    private float mLastX;
    private float mLastY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                float currentY = event.getY();
                mImageMatrix.postTranslate(currentX - mLastX, currentY - mLastY);
                mLastX = currentX;
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();
        return true;
    }
}

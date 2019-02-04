package com.lcw.view.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * 贴纸布局
 * Create by: chenWei.li
 * Date: 2019/2/3
 * Time: 6:57 PM
 * Email: lichenwei.me@foxmail.com
 */
public class StickerLayout extends View implements View.OnTouchListener {

    private Paint mPaint;

    //记录当前操作的贴纸对象
    private Sticker mStick;
    //记录点坐标，减少对象在onTouch中的创建
    private PointF mFirstPoint = new PointF();
    private PointF mSecondPoint = new PointF();


    public StickerLayout(Context context) {
        super(context);
        init(context);
    }

    public StickerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化操作
     *
     * @param context
     */
    private void init(Context context) {

        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);

        //设置触摸监听
        setOnTouchListener(this);

    }

    /**
     * 添加贴纸
     *
     * @param sticker
     */
    public void addSticker(Sticker sticker) {
        StickerManager.getInstance().addSticker(sticker);
        invalidate();
    }

    /**
     * 移除贴纸
     * @param sticker
     */
    public void removeSticker(Sticker sticker){
        StickerManager.getInstance().removeSticker(sticker);
        invalidate();
    }

    /**
     * 清空贴纸
     */
    public void clear(){
        StickerManager.getInstance().removeAllSticker();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Sticker> stickerList = StickerManager.getInstance().getStickerList();
        for (int i = 0; i < stickerList.size(); i++) {
            Sticker sticker = stickerList.get(i);
            sticker.onDraw(canvas, mPaint);
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mStick = StickerManager.getInstance().getSticker(event.getX(), event.getY());
                if (mStick != null) {
                    //有触摸到贴纸
                    mStick.setMode(Sticker.MODE_SINGLE);
                    mStick.setLastSinglePoint(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mStick != null && event.getPointerCount() == 2) {
                    mStick.setMode(Sticker.MODE_MULTIPLE);
                    //记录双指的点位置
                    mFirstPoint.set(event.getX(0), event.getY(0));
                    mSecondPoint.set(event.getX(1), event.getY(1));
                    //计算双指之间向量
                    mStick.setLastDistanceVector(mFirstPoint.x - mSecondPoint.x, mFirstPoint.y - mSecondPoint.y);
                    //计算双指之间距离
                    mStick.mLastDistance = mStick.calculateDistance(mFirstPoint, mSecondPoint);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStick != null) {
                    if (mStick.getMode() == Sticker.MODE_SINGLE) {
                        mStick.translate(event.getX() - mStick.getLastSinglePoint().x, event.getY() - mStick.getLastSinglePoint().y);
                        mStick.getLastSinglePoint().set(event.getX(), event.getY());
                    }

                    if (mStick.getMode() == Sticker.MODE_MULTIPLE && event.getPointerCount() == 2) {
                        //记录双指的点位置
                        mFirstPoint.set(event.getX(0), event.getY(0));
                        mSecondPoint.set(event.getX(1), event.getY(1));
                        //操作自由缩放
                        float distance = mStick.calculateDistance(mFirstPoint, mSecondPoint);
                        //根据双指移动的距离获取缩放因子
                        float scale = distance / mStick.mLastDistance;
                        mStick.scale(scale, scale, mStick.getMidPoint().x, mStick.getMidPoint().y);
                        mStick.mLastDistance = distance;
                        //操作自由旋转
                        mStick.setDistanceVector(mFirstPoint.x - mSecondPoint.x, mFirstPoint.y - mSecondPoint.y);
                        mStick.rotate(mStick.calculateDegrees(mStick.getLastDistanceVector(), mStick.getDistanceVector()), mStick.getMidPoint().x, mStick.getMidPoint().y);
                        mStick.setLastDistanceVector(mStick.getDistanceVector().x, mStick.getDistanceVector().y);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mStick != null) {
                    mStick.reset();
                }
                break;
        }
        if (mStick != null) {
            invalidate();
        }
        return true;
    }
}

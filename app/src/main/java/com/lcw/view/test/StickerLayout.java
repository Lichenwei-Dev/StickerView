package com.lcw.view.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * 贴纸布局
 * Create by: chenWei.li
 * Date: 2019/2/3
 * Time: 6:57 PM
 * Email: lichenwei.me@foxmail.com
 */
public class StickerLayout extends View implements View.OnTouchListener {

    private Context mContext;

    //画笔
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
     */
    private void init(Context context) {
        this.mContext = context;
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
        int size = StickerManager.getInstance().getStickerList().size();
        if (size < 9) {
            StickerManager.getInstance().addSticker(sticker);
            invalidate();
        } else {
            Toast.makeText(mContext, "贴纸最大数量不能超过9个", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 移除贴纸
     *
     * @param sticker
     */
    public void removeSticker(Sticker sticker) {
        StickerManager.getInstance().removeSticker(sticker);
        invalidate();
    }

    /**
     * 清空贴纸
     */
    public void removeAllSticker() {
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
                mStick = StickerManager.getInstance().getDelButton(event.getX(), event.getY());
                if (mStick != null) {
                    removeSticker(mStick);
                    mStick = null;
                }
                mStick = StickerManager.getInstance().getSticker(event.getX(), event.getY());
                if (mStick != null) {
                    //有触摸到贴纸
                    mStick.setMode(Sticker.MODE_SINGLE);
                    mStick.mLastSinglePoint.set(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    if (mStick == null) {
                        //处理双指触摸屏幕，第一指没有触摸到贴纸，第二指触摸到贴纸情况
                        mStick = StickerManager.getInstance().getSticker(event.getX(1), event.getY(1));
                    }
                    if (mStick != null) {
                        mStick.setMode(Sticker.MODE_MULTIPLE);
                        //记录双指的点位置
                        mFirstPoint.set(event.getX(0), event.getY(0));
                        mSecondPoint.set(event.getX(1), event.getY(1));
                        //计算双指之间向量
                        mStick.mLastDistanceVector.set(mFirstPoint.x - mSecondPoint.x, mFirstPoint.y - mSecondPoint.y);
                        //计算双指之间距离
                        mStick.mLastDistance = mStick.calculateDistance(mFirstPoint, mSecondPoint);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStick != null) {
                    if (mStick.getMode() == Sticker.MODE_SINGLE) {
                        mStick.translate(event.getX() - mStick.mLastSinglePoint.x, event.getY() - mStick.mLastSinglePoint.y);
                        mStick.mLastSinglePoint.set(event.getX(), event.getY());
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
                        mStick.mDistanceVector.set(mFirstPoint.x - mSecondPoint.x, mFirstPoint.y - mSecondPoint.y);
                        mStick.rotate(mStick.calculateDegrees(mStick.mLastDistanceVector, mStick.mDistanceVector), mStick.getMidPoint().x, mStick.getMidPoint().y);
                        mStick.mLastDistanceVector.set(mStick.mDistanceVector.x, mStick.mDistanceVector.y);
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

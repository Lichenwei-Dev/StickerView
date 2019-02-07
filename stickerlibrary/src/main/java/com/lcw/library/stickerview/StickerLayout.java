package com.lcw.library.stickerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * 贴纸布局（管理分发各种贴纸处理事件）
 * Create by: chenWei.li
 * Date: 2019/2/3
 * Time: 6:57 PM
 * Email: lichenwei.me@foxmail.com
 */
public class StickerLayout extends View implements View.OnTouchListener {

    private Context mContext;
    private Paint mPaint;

    //记录当前操作的贴纸对象
    private Sticker mStick;

    public StickerLayout(Context context) {
        super(context);
        init(context);
    }

    public StickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化操作
     */
    private void init(Context context) {
        this.mContext = context;
        //设置触摸监听
        setOnTouchListener(this);
    }

    public Paint getPaint() {
        if (mPaint == null) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(2);
        }
        return mPaint;
    }

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
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
            StickerManager.getInstance().setFocusSticker(sticker);
            invalidate();
        } else {
            Toast.makeText(mContext, "贴纸最大数量不能超过9个", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 移除贴纸（只有在贴纸聚焦的时候才可以删除，避免误触碰）
     *
     * @param sticker
     */
    public void removeSticker(Sticker sticker) {
        if (sticker.isFocus()) {
            StickerManager.getInstance().removeSticker(sticker);
            invalidate();
        }
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
        Sticker focusSticker = null;
        for (int i = 0; i < stickerList.size(); i++) {
            Sticker sticker = stickerList.get(i);
            if (sticker.isFocus()) {
                focusSticker = sticker;
            } else {
                sticker.onDraw(canvas, getPaint());
            }
        }
        if (focusSticker != null) {
            focusSticker.onDraw(canvas, getPaint());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                //判断是否按到删除按钮
                mStick = StickerManager.getInstance().getDelButton(event.getX(), event.getY());
                if (mStick != null) {
                    removeSticker(mStick);
                    mStick = null;
                }
                //单指是否触摸到贴纸
                mStick = StickerManager.getInstance().getSticker(event.getX(), event.getY());
                if (mStick == null) {
                    if (event.getPointerCount() == 2) {
                        //处理双指触摸屏幕，第一指没有触摸到贴纸，第二指触摸到贴纸情况
                        mStick = StickerManager.getInstance().getSticker(event.getX(1), event.getY(1));
                    }
                }
                if (mStick != null) {
                    StickerManager.getInstance().setFocusSticker(mStick);
                }
                break;
            default:
                break;
        }
        if (mStick != null) {
            mStick.onTouch(event);
        } else {
            StickerManager.getInstance().clearAllFocus();
        }
        invalidate();
        return true;
    }
}

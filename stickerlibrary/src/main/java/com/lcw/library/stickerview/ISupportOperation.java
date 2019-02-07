package com.lcw.library.stickerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * 贴纸的动作接口（支持拖动，缩放，旋转，绘制，触摸）
 * Create by: chenWei.li
 * Date: 2019/2/4
 * Time: 12:58 AM
 * Email: lichenwei.me@foxmail.com
 */
public interface ISupportOperation {

    /**
     * 平移操作
     *
     * @param dx
     * @param dy
     */
    void translate(float dx, float dy);

    /**
     * 缩放操作
     *
     * @param sx
     * @param sy
     */
    void scale(float sx, float sy);

    /**
     * 旋转操作
     *
     * @param degrees
     */
    void rotate(float degrees);


    /**
     * 绘制操作
     *
     * @param canvas
     * @param paint
     */
    void onDraw(Canvas canvas, Paint paint);

    /**
     * 触摸操作
     *
     * @param event
     */
    void onTouch(MotionEvent event);

}

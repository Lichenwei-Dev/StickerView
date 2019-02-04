package com.lcw.view.test;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 贴纸的动作接口（支持拖动，缩放，旋转，绘制）
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
     * @param px
     * @param py
     */
    void scale(float sx, float sy, float px, float py);

    /**
     * 旋转操作
     *
     * @param degrees
     * @param px
     * @param py
     */
    void rotate(float degrees, float px, float py);


    /**
     * 绘制操作
     *
     * @param canvas
     * @param paint
     */
    void onDraw(Canvas canvas, Paint paint);

}

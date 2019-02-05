package com.lcw.view.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;

/**
 * 贴纸类（处理手势动作以及其他业务逻辑）
 * Create by: chenWei.li
 * Date: 2019/2/3
 * Time: 8:44 PM
 * Email: lichenwei.me@foxmail.com
 */
public class Sticker extends BaseSticker {

    protected PointF mLastSinglePoint = new PointF();//记录上一次单指触摸屏幕的点坐标
    protected PointF mLastDistanceVector = new PointF();//记录上一次双指之间的向量
    protected PointF mDistanceVector = new PointF();//记录当前双指之间的向量
    protected float mLastDistance;//记录上一次双指之间的距离

    public Sticker(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    /**
     * 平移操作
     *
     * @param dx
     * @param dy
     */
    @Override
    public void translate(float dx, float dy) {
        matrix.postTranslate(dx, dy);
        updatePoints();
    }

    /**
     * 缩放操作
     *
     * @param sx
     * @param sy
     * @param px
     * @param py
     */
    @Override
    public void scale(float sx, float sy, float px, float py) {
        matrix.postScale(sx, sy, px, py);
        updatePoints();
    }

    /**
     * 旋转操作
     *
     * @param degrees
     * @param px
     * @param py
     */
    @Override
    public void rotate(float degrees, float px, float py) {
        matrix.postRotate(degrees, px, py);
        updatePoints();
    }

    /**
     * 重置状态
     */
    public void reset() {
        mLastDistance = 0f;
        mLastSinglePoint.set(0f, 0f);
        mLastDistanceVector.set(0f, 0f);
        mDistanceVector.set(0f, 0f);
        mMidPointF.set(0f, 0f);
    }

    /**
     * 计算两点之间的距离
     */
    public float calculateDistance(PointF firstPointF, PointF secondPointF) {
        float x = firstPointF.x - secondPointF.x;
        float y = firstPointF.y - secondPointF.y;
        return (float) Math.sqrt(x * x + y * y);
    }


    /**
     * 计算旋转角度
     *
     * @param lastVector
     * @param currentVector
     * @return
     */
    public float calculateDegrees(PointF lastVector, PointF currentVector) {
        float lastDegrees = (float) Math.atan2(lastVector.y, lastVector.x);
        float currentDegrees = (float) Math.atan2(currentVector.y, currentVector.x);
        return (float) Math.toDegrees(currentDegrees - lastDegrees);
    }

    /**
     * 当矩阵发生变化的时候，更新坐标点（src坐标点经过matrix映射变成了dst坐标点）
     */
    private void updatePoints() {
        matrix.mapPoints(mDstPoints, mSrcPoints);
    }

}

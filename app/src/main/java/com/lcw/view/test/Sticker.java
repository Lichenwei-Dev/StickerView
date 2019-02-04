package com.lcw.view.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * 贴纸类（处理手势动作）
 * Create by: chenWei.li
 * Date: 2019/2/3
 * Time: 8:44 PM
 * Email: lichenwei.me@foxmail.com
 */
public class Sticker extends BaseSticker {

    private PointF mLastSinglePoint = new PointF();//记录上一次单指触摸屏幕的点坐标
    private PointF mLastDistanceVector = new PointF();//记录上一次双指之间的向量
    private PointF mDistanceVector = new PointF();//记录当前双指之间的向量
    public float mLastDistance;//记录上一次双指之间的距离

    public Sticker(Bitmap bitmap){
        super(bitmap);
    }

    public PointF getLastSinglePoint() {
        return mLastSinglePoint;
    }

    public void setLastSinglePoint(float x, float y) {
        this.mLastSinglePoint.set(x, y);
    }


    public PointF getLastDistanceVector() {
        return mLastDistanceVector;
    }

    public void setLastDistanceVector(float x, float y) {
        this.mLastDistanceVector.set(x, y);
    }

    public PointF getDistanceVector() {
        return mDistanceVector;
    }

    public void setDistanceVector(float x, float y) {
        this.mDistanceVector.set(x, y);
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
        matrix.mapPoints(mDstPoints, mScrPoints);
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
        matrix.mapPoints(mDstPoints, mScrPoints);
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
        matrix.mapPoints(mDstPoints, mScrPoints);
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, matrix, paint);
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


}

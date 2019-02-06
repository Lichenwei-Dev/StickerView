package com.lcw.view.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * 贴纸类（手势与贴纸的关系）
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

    //记录点坐标，减少对象在onTouch中的创建
    private PointF mFirstPoint = new PointF();
    private PointF mSecondPoint = new PointF();

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
        mMode = MODE_NONE;
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
        //更新贴纸点坐标
        matrix.mapPoints(mDstPoints, mSrcPoints);
        //更新贴纸中心点坐标
        mMidPointF.set(mDstPoints[8], mDstPoints[9]);
    }


    /**
     * 处理触摸事件
     *
     * @param event
     * @return
     */
    public void onTouch(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // TODO: 2019/2/6 需要处理层级关系，不然当贴纸重叠的时候会出现bug
                StickerManager.getInstance().setFocusSticker(this);
                //有触摸到贴纸
                mMode = Sticker.MODE_SINGLE;
                //记录按下的位置
                mLastSinglePoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    mMode = Sticker.MODE_MULTIPLE;
                    //记录双指的点位置
                    mFirstPoint.set(event.getX(0), event.getY(0));
                    mSecondPoint.set(event.getX(1), event.getY(1));
                    //计算双指之间向量
                    mLastDistanceVector.set(mFirstPoint.x - mSecondPoint.x, mFirstPoint.y - mSecondPoint.y);
                    //计算双指之间距离
                    mLastDistance = calculateDistance(mFirstPoint, mSecondPoint);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == MODE_SINGLE) {
                    translate(event.getX() - mLastSinglePoint.x, event.getY() - mLastSinglePoint.y);
                    mLastSinglePoint.set(event.getX(), event.getY());
                }

                if (mMode == MODE_MULTIPLE && event.getPointerCount() == 2) {
                    //记录双指的点位置
                    mFirstPoint.set(event.getX(0), event.getY(0));
                    mSecondPoint.set(event.getX(1), event.getY(1));
                    //操作自由缩放
                    float distance = calculateDistance(mFirstPoint, mSecondPoint);
                    //根据双指移动的距离获取缩放因子
                    float scale = distance / mLastDistance;
                    scale(scale, scale, mMidPointF.x, mMidPointF.y);
                    mLastDistance = distance;
                    //操作自由旋转
                    mDistanceVector.set(mFirstPoint.x - mSecondPoint.x, mFirstPoint.y - mSecondPoint.y);
                    rotate(calculateDegrees(mLastDistanceVector, mDistanceVector), mMidPointF.x, mMidPointF.y);
                    mLastDistanceVector.set(mDistanceVector.x, mDistanceVector.y);
                }
                break;
            case MotionEvent.ACTION_UP:
                reset();
                break;
        }
    }

}

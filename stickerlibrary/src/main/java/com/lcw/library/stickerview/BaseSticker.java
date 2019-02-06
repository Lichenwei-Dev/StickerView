package com.lcw.library.stickerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * 贴纸的基类（存放贴纸的基本的属性）
 * Create by: chenWei.li
 * Date: 2019/2/4
 * Time: 12:55 AM
 * Email: lichenwei.me@foxmail.com
 */
public abstract class BaseSticker implements ISupportOperation {

    private Bitmap mStickerBitmap;//贴纸图像
    private Bitmap mDelBitmap;//贴纸图像
    private Matrix mMatrix;//维护图像变化的矩阵
    private boolean isFocus;//当前是否聚焦
    protected int mMode;//当前模式

    private float[] mSrcPoints;//矩阵变换前的点坐标
    private float[] mDstPoints;//矩阵变换后的点坐标
    private RectF mStickerBound;//贴纸范围
    private RectF mDelBound;//删除按钮范围
    private PointF mMidPointF;//贴纸中心的点坐标

    public static final int MODE_NONE = 0;//初始状态
    public static final int MODE_SINGLE = 1;//标志是否可移动
    public static final int MODE_MULTIPLE = 2;//标志是否可缩放，旋转

    private static final int PADDING = 30;//避免图像与边框太近，这里设置一个边距


    public BaseSticker(Context context, Bitmap bitmap) {
        this.mStickerBitmap = bitmap;
        mMatrix = new Matrix();
        mMidPointF = new PointF();

        mSrcPoints = new float[]{
                0, 0,//左上
                bitmap.getWidth(), 0,//右上
                bitmap.getWidth(), bitmap.getHeight(),//右下
                0, bitmap.getHeight(),//左下
                bitmap.getWidth() / 2, bitmap.getHeight() / 2//中间点
        };
        mDstPoints = mSrcPoints.clone();
        mStickerBound = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

        mDelBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_delete);
        mDelBound = new RectF(0 - mDelBitmap.getWidth() / 2 - PADDING, 0 - mDelBitmap.getHeight() / 2 - PADDING, mDelBitmap.getWidth() / 2 + PADDING, mDelBitmap.getHeight() / 2 + PADDING);

        //将贴纸默认移动到屏幕中间
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        float dx = displayMetrics.widthPixels / 2 - mStickerBitmap.getWidth() / 2;
        float dy = displayMetrics.heightPixels / 2 - mStickerBitmap.getHeight() / 2;
        translate(dx, dy);
        //将贴纸默认缩小1/2
        scale(0.5f, 0.5f);
    }

    public Bitmap getBitmap() {
        return mStickerBitmap;
    }

    public RectF getStickerBitmapBound() {
        return mStickerBound;
    }

    public RectF getDelBitmapBound() {
        return mDelBound;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    /**
     * 平移操作
     *
     * @param dx
     * @param dy
     */
    @Override
    public void translate(float dx, float dy) {
        mMatrix.postTranslate(dx, dy);
        updatePoints();
    }

    /**
     * 缩放操作
     *
     * @param sx
     * @param sy
     */
    @Override
    public void scale(float sx, float sy) {
        mMatrix.postScale(sx, sy, mMidPointF.x, mMidPointF.y);
        updatePoints();
    }

    /**
     * 旋转操作
     *
     * @param degrees
     */
    @Override
    public void rotate(float degrees) {
        mMatrix.postRotate(degrees, mMidPointF.x, mMidPointF.y);
        updatePoints();
    }

    /**
     * 当矩阵发生变化的时候，更新坐标点（src坐标点经过matrix映射变成了dst坐标点）
     */
    private void updatePoints() {
        //更新贴纸点坐标
        mMatrix.mapPoints(mDstPoints, mSrcPoints);
        //更新贴纸中心点坐标
        mMidPointF.set(mDstPoints[8], mDstPoints[9]);
    }

    /**
     * 绘制贴纸自身
     *
     * @param canvas
     * @param paint
     */
    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        //绘制贴纸
        canvas.drawBitmap(mStickerBitmap, mMatrix, paint);
        if (isFocus) {
            //绘制贴纸边框
            canvas.drawLine(mDstPoints[0] - PADDING, mDstPoints[1] - PADDING, mDstPoints[2] + PADDING, mDstPoints[3] - PADDING, paint);
            canvas.drawLine(mDstPoints[2] + PADDING, mDstPoints[3] - PADDING, mDstPoints[4] + PADDING, mDstPoints[5] + PADDING, paint);
            canvas.drawLine(mDstPoints[4] + PADDING, mDstPoints[5] + PADDING, mDstPoints[6] - PADDING, mDstPoints[7] + PADDING, paint);
            canvas.drawLine(mDstPoints[6] - PADDING, mDstPoints[7] + PADDING, mDstPoints[0] - PADDING, mDstPoints[1] - PADDING, paint);
            //绘制移除按钮
            canvas.drawBitmap(mDelBitmap, mDstPoints[0] - mDelBitmap.getWidth() / 2 - PADDING, mDstPoints[1] - mDelBitmap.getHeight() / 2 - PADDING, paint);
        }
    }

}

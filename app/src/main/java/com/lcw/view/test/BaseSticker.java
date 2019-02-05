package com.lcw.view.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.lcw.view.R;

/**
 * 贴纸的基类（存放贴纸的基本的属性）
 * Create by: chenWei.li
 * Date: 2019/2/4
 * Time: 12:55 AM
 * Email: lichenwei.me@foxmail.com
 */
public abstract class BaseSticker implements ISupportOperation {

    protected Bitmap mStickerBitmap;//贴纸图像
    protected Bitmap mDelBitmap;//贴纸图像
    protected Matrix matrix;//维护图像变化的矩阵
    private int mode;//当前模式

    protected float[] mSrcPoints;//矩阵变换前的点坐标
    protected float[] mDstPoints;//矩阵变换后的点坐标
    protected RectF mStickerBound;//贴纸范围
    protected RectF mDelBound;//删除按钮范围
    protected PointF mMidPointF = new PointF();//贴纸中心的点坐标


    public static final int MODE_SINGLE = 1;//标志是否可移动
    public static final int MODE_MULTIPLE = 2;//标志是否可缩放，旋转

    public static final int PADDING = 20;

    public BaseSticker(Context context, Bitmap bitmap) {
        this.mStickerBitmap = bitmap;
        matrix = new Matrix();
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

    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public Bitmap getBitmap() {
        return mStickerBitmap;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public RectF getStickerBitmapBound() {
        return mStickerBound;
    }

    public RectF getDelBitmapBound() {
        return mDelBound;
    }

    /**
     * 绘制
     *
     * @param canvas
     * @param paint
     */
    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        //绘制贴纸
        canvas.drawBitmap(mStickerBitmap, matrix, paint);
        //绘制贴纸边框
        canvas.drawLine(mDstPoints[0] - PADDING, mDstPoints[1] - PADDING, mDstPoints[2] + PADDING, mDstPoints[3] - PADDING, paint);
        canvas.drawLine(mDstPoints[2] + PADDING, mDstPoints[3] - PADDING, mDstPoints[4] + PADDING, mDstPoints[5] + PADDING, paint);
        canvas.drawLine(mDstPoints[4] + PADDING, mDstPoints[5] + PADDING, mDstPoints[6] - PADDING, mDstPoints[7] + PADDING, paint);
        canvas.drawLine(mDstPoints[6] - PADDING, mDstPoints[7] + PADDING, mDstPoints[0] - PADDING, mDstPoints[1] - PADDING, paint);
        //绘制移除按钮
        canvas.drawBitmap(mDelBitmap, mDstPoints[0] - mDelBitmap.getWidth() / 2 - PADDING, mDstPoints[1] - mDelBitmap.getHeight() / 2 - PADDING, paint);
    }

    /**
     * 获取图像中心点
     *
     * @return
     */
    public PointF getMidPoint() {
        mMidPointF.set(mDstPoints[8], mDstPoints[9]);
        return mMidPointF;
    }
}

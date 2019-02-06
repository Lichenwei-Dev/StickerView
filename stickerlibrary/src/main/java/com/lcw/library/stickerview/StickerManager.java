package com.lcw.library.stickerview;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

/**
 * 贴纸素材管理器
 * Create by: chenWei.li
 * Date: 2019/2/3
 * Time: 8:44 PM
 * Email: lichenwei.me@foxmail.com
 */
public class StickerManager {

    private static volatile StickerManager mInstance;

    private List<Sticker> mStickerList = new ArrayList<>();

    public static StickerManager getInstance() {
        if (mInstance == null) {
            synchronized (StickerManager.class) {
                if (mInstance == null) {
                    mInstance = new StickerManager();
                }
            }
        }
        return mInstance;
    }

    public void addSticker(Sticker sticker) {
        mStickerList.add(sticker);
    }

    public List<Sticker> getStickerList() {
        return mStickerList;
    }

    /**
     * 移除指定贴纸
     *
     * @param sticker
     */
    public void removeSticker(Sticker sticker) {
        Bitmap bitmap = sticker.getBitmap();
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
        }
        mStickerList.remove(sticker);

    }

    /**
     * 移除所有贴纸
     */
    public void removeAllSticker() {
        for (int i = 0; i < mStickerList.size(); i++) {
            Bitmap bitmap = mStickerList.get(i).getBitmap();
            if (bitmap != null && bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        mStickerList.clear();
    }

    /**
     * 设置当前贴纸为焦点贴纸
     *
     * @param focusSticker
     */
    public void setFocusSticker(Sticker focusSticker) {
        for (int i = 0; i < mStickerList.size(); i++) {
            Sticker sticker = mStickerList.get(i);
            if (sticker == focusSticker) {
                sticker.setFocus(true);
            } else {
                sticker.setFocus(false);
            }
        }
    }

    /**
     * 清除所有焦点
     */
    public void clearAllFocus() {
        for (int i = 0; i < mStickerList.size(); i++) {
            Sticker sticker = mStickerList.get(i);
            sticker.setFocus(false);
        }
    }

    /**
     * 根据触摸坐标返回当前触摸的贴纸
     *
     * @param x
     * @param y
     * @return
     */
    public Sticker getSticker(float x, float y) {

        float[] dstPoints = new float[2];
        float[] srcPoints = new float[]{x, y};

        for (int i = mStickerList.size() - 1; i >= 0; i--) {
            Sticker sticker = mStickerList.get(i);
            Matrix matrix = new Matrix();
            sticker.getMatrix().invert(matrix);
            matrix.mapPoints(dstPoints, srcPoints);
            if (sticker.getStickerBitmapBound().contains(dstPoints[0], dstPoints[1])) {
                return sticker;
            }
        }
        return null;
    }

    /**
     * 根据触摸是否触摸到删除按钮，返回对应删除按钮的贴纸
     *
     * @param x
     * @param y
     * @return
     */
    public Sticker getDelButton(float x, float y) {

        float[] dstPoints = new float[2];
        float[] srcPoints = new float[]{x, y};

        for (int i = mStickerList.size() - 1; i >= 0; i--) {
            Sticker sticker = mStickerList.get(i);
            Matrix matrix = new Matrix();
            sticker.getMatrix().invert(matrix);
            matrix.mapPoints(dstPoints, srcPoints);
            if (sticker.getDelBitmapBound().contains(dstPoints[0], dstPoints[1])) {
                return sticker;
            }
        }
        return null;

    }


}

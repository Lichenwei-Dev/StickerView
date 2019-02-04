package com.lcw.view.test;

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

    public void removeSticker(Sticker sticker) {
        mStickerList.remove(sticker);
    }

    public void removeAllSticker() {
        mStickerList.clear();
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
            if (sticker.getBitmapBound().contains(dstPoints[0], dstPoints[1])) {
                return sticker;
            }
        }
        return null;
    }
}

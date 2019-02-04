package com.lcw.view;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lcw.view.test.Sticker;
import com.lcw.view.test.StickerLayout;

public class MainActivity extends AppCompatActivity {

    private StickerLayout mStickerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStickerLayout = findViewById(R.id.sl_sticker_layout);

        findViewById(R.id.iv_sticker_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sticker sticker = new Sticker(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.sticker_01));
                mStickerLayout.addSticker(sticker);
            }
        });

        findViewById(R.id.iv_sticker_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sticker sticker = new Sticker(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.sticker_02));
                mStickerLayout.addSticker(sticker);
            }
        });

        findViewById(R.id.iv_sticker_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sticker sticker = new Sticker(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.sticker_03));
                mStickerLayout.addSticker(sticker);
            }
        });

        findViewById(R.id.iv_sticker_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sticker sticker = new Sticker(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.mipmap.sticker_04));
                mStickerLayout.addSticker(sticker);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStickerLayout.clear();
    }
}

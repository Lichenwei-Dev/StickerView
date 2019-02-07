# Android开发之仿微博贴纸效果实现——StickerView

#### 效果图：
![](https://github.com/Lichenwei-Dev/StickerView/blob/master/screenshot/stickerview.gif)

1、如何在项目中引入该贴纸库：

```
//gradle版本在3.0以下引入此行
compile 'com.lcw.library:StickeView:1.0.0'

//gradle版本在3.0以上引入此行
implementation 'com.lcw.library:StickeView:1.0.0'
```

2、布局设置：
```
 <com.lcw.library.stickerview.StickerLayout
        android:id="@+id/sl_sticker_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

```


3、添加贴纸：
```
                Sticker sticker = new Sticker(mContext,mBitmap);
                mStickerLayout.addSticker(sticker);
```

就这么简单，持续更新，欢迎各种优化建议~

具体实现可以参考文章：
![Android开发之仿微博贴纸效果实现——基础篇](https://www.jianshu.com/p/258059a08cad)
![Android开发之仿微博贴纸效果实现——进阶](https://www.jianshu.com/p/f1b0e4adae34)


欢迎Star，欢迎Fork
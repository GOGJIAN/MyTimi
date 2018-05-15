package com.jianjian.android.mytimi.tools;

import android.graphics.Bitmap;
import android.util.LruCache;

public interface ImageCache {
    void put(String filename,Bitmap bitmap);
    Bitmap get(String filename);
}

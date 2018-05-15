package com.jianjian.android.mytimi.tools;

import android.graphics.Bitmap;

public class DoubleCache implements ImageCache {
    private MemoryCache mMemoryCache = new MemoryCache();
    private DiskCache mDiskCache = new DiskCache();
    @Override
    public void put(String filename, Bitmap bitmap) {
        mDiskCache.put(filename, bitmap);
        mMemoryCache.put(filename,bitmap);
    }

    @Override
    public Bitmap get(String filename) {
        Bitmap bitmap = mMemoryCache.get(filename);
        if(bitmap!=null)
            return bitmap;
        else {
            bitmap = mDiskCache.get(filename);
            if(bitmap!=null)
            mMemoryCache.put(filename,bitmap);
        }
        return bitmap;
    }
}

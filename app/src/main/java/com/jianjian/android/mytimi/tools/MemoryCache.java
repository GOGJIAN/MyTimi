package com.jianjian.android.mytimi.tools;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache implements ImageCache {
    private LruCache<String,Bitmap> mMemoryCache;

    public MemoryCache(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize = maxMemory/4;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount()/1024;
            }
        };
    }
    @Override
    public void put(String filename, Bitmap bitmap) {
        mMemoryCache.put(filename,bitmap);
    }

    @Override
    public Bitmap get(String filename) {
        return mMemoryCache.get(filename);
    }
}

package com.jianjian.android.mytimi.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DiskCache implements ImageCache {
    @Override
    public void put(String filename, Bitmap bitmap) {
        File file = new File(Content.path+filename);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(String filename) {
        return BitmapFactory.decodeFile(Content.path+filename);
    }
}

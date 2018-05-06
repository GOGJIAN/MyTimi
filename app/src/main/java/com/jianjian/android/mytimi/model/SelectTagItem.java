package com.jianjian.android.mytimi.model;

import android.graphics.drawable.Icon;
import android.media.Image;

public class SelectTagItem {
    private Long id;
    private String mIcon;
    private String mTag;
    private int Color;



    public SelectTagItem(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }
}

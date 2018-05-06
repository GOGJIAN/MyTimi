package com.jianjian.android.mytimi.model;

import com.jianjian.android.mytimi.R;

import java.util.ArrayList;

public class TagLab {
    private static TagLab tagLab;
    private ArrayList<SelectTagItem> tags;
    private TagLab(){
        tags = new ArrayList<>();
        int icon[] = {
                R.drawable.ic_common,
                R.drawable.ic_communication,
                R.drawable.ic_live,
                R.drawable.ic_traffic,
                R.drawable.ic_cake,
                R.drawable.ic_card,
                R.drawable.ic_drink,
                R.drawable.ic_food,
                R.drawable.ic_movie,
                R.drawable.ic_study};
        int color[] = {
                R.color.commonColor,
                R.color.communicationColor,
                R.color.liveColor,
                R.color.trafficColor,
                R.color.cakeColor,
                R.color.cardColor,
                R.color.drinkColor,
                R.color.foodColor,
                R.color.movieColor,
                R.color.studyColor};
        String strings[] = {
                "一般",
                "通讯",
                "日用品",
                "交通",
                "点心",
                "卡片",
                "休闲",
                "餐饮",
                "电影",
                "学习"};
        for (int i = 0; i < icon.length; i++) {
            SelectTagItem tagItem = new SelectTagItem();
            tagItem.setIcon(String.valueOf(icon[i]));
            tagItem.setColor(color[i]);
            tagItem.setTag(strings[i]);
            tags.add(tagItem);
        }
    }
    public static TagLab getInstance(){
        if(tagLab==null)
            tagLab = new TagLab();
        return tagLab;
    }

    public ArrayList<SelectTagItem> getTags(){
        return tags;
    }
}

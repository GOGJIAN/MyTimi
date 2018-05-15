package com.jianjian.android.mytimi.tools;

import com.google.gson.Gson;
import com.jianjian.android.mytimi.model.NotificationItem;

import java.io.IOException;

public class TimiFetchr {
    public String getNotification(){
        try {
            String jsonString = new HttpUtil().getUrlString(Content.url+"notify");
            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

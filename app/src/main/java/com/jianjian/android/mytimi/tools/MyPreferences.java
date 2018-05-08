package com.jianjian.android.mytimi.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyPreferences {
    private static final String PREF_BACKGROUND_PATH = "background";

    public static String getBackgroundPath(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_BACKGROUND_PATH,null);
    }

    public static void setBackgroundPath(Context context,String path){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_BACKGROUND_PATH,path)
                .apply();
    }
}

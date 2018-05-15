package com.jianjian.android.mytimi.tools;

import android.os.Environment;

import com.jianjian.android.mytimi.R;

public class Content {
    public static final int INCOME_TYPE = 0;
    public static final int PAY_TYPE = 1;

    public static final String author = "com.jianjian.android.mytimi.provider";

    public static final int REQUEST_CAPTURE = 3;
    public static final int REQUEST_SCAN_N = 4;
    public static final int REQUEST_CROP = 5;
    public static final int REQUEST_CODE_ASK_STORAGE = 6;

    public static final String path = Environment.getExternalStorageDirectory()+"/myTimi/";

    public static final String url = "http://192.168.43.59:8080/timi/";
//public static final String url = "http://www.baidu.com";
}

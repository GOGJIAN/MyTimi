package com.jianjian.android.mytimi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jianjian.android.mytimi.service.PollService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction()!=null&&intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            PollService.setServiceAlarm(context);
        }
    }
}

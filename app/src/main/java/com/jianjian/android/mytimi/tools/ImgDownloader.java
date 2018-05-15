package com.jianjian.android.mytimi.tools;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public class ImgDownloader<T> extends HandlerThread {
    private static final String TAG = "ImgDownloader";
    private boolean isQuit = false;

    private static final int MESSAGE_DOWNLOAD = 0;
    private ConcurrentHashMap<T,String> requestMap = new ConcurrentHashMap<>();
    private Handler requestHandler;
    private Handler responseHandler;

    private ImgDownloadListener<T> mTImgDownloadListener;

    public interface ImgDownloadListener<T>{
        void onImgDownLoaded(T target,Bitmap img);
    }

    public void setImgDownloadListener(ImgDownloadListener<T> listener){
        this.mTImgDownloadListener = listener;
    }

    public ImgDownloader(Handler handler) {
        super(TAG);
        responseHandler = handler;
    }

    @Override
    public boolean quit() {
        isQuit = true;
        return super.quit();
    }

    public void queueUrl(T target, String url){
        if(url==null){
            requestMap.remove(target);
        }else{
            requestMap.put(target,url);
            requestHandler.obtainMessage(MESSAGE_DOWNLOAD,target)
                    .sendToTarget();
        }
    }

    public void clearQueue(){
        requestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        requestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==MESSAGE_DOWNLOAD){
                    T target = (T)msg.obj;
                    handleRequest(target);
                }
            }
        };
    }


    private void handleRequest(final T target){
       final String url = requestMap.get(target);
       try {
           byte[] bytes = new HttpUtil().getUrlBytes(url);
           final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
           responseHandler.post(new Runnable() {
               @Override
               public void run() {
                   if(!requestMap.get(target).equals(url) ||isQuit)
                       return;
                   mTImgDownloadListener.onImgDownLoaded(target,bitmap);
               }
           });
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}

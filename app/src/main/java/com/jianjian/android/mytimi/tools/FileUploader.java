package com.jianjian.android.mytimi.tools;

import android.app.DownloadManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.UUID;

public class FileUploader {

    private static final int TIME_OUT = 10*1000;
    private static final String CHARSET = "utf-8";
    public void uploadFile(File file){
        String BOUNDARY = UUID.randomUUID().toString();
        String CONTENT_TYPE = "multipart/form-data";
        String PREFIX = "--";
        String LINE_END = "\r\n";
        try {
            URL url = new URL(Content.url+"test");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Charset",CHARSET);
            connection.setRequestProperty("connection","keep-alive");
            connection.setRequestProperty("Content-type",CONTENT_TYPE+";boundary="+BOUNDARY);
            if(file!=null){
                OutputStream os = connection.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                StringBuffer sb = new StringBuffer();

                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);

                sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"").append(LINE_END);
                sb.append("Content-type:application/octet-stream;charset=\"" + CHARSET).append(LINE_END);
                sb.append(LINE_END);

                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte bytes[] = new byte[1024];
                int len = 0;
                while((len = is.read(bytes))!=-1){
                    dos.write(bytes,0,len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                dos.write((PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes());
                dos.flush();
                int res = connection.getResponseCode();
                if(res==HttpURLConnection.HTTP_OK){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

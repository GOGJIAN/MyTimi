package com.jianjian.android.mytimi.tools;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

public class HttpUtil {
    public byte[] getUrlBytes(String urlSpec) throws IOException{

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();;
        try {
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException("Internet Connection Error");
            }
            byte buffer[] = new byte[1024];
            int len = 0;
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while((len=is.read(buffer))!=-1){
                out.write(buffer,0,len);
            }

            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }

    }
    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }
}

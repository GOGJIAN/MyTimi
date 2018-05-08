package com.jianjian.android.mytimi.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jianjian.android.mytimi.MainActivity;
import com.jianjian.android.mytimi.R;

import java.io.File;
import java.io.FileNotFoundException;

import static com.jianjian.android.mytimi.tools.Content.REQUEST_CAPTURE;
import static com.jianjian.android.mytimi.tools.Content.REQUEST_CODE_ASK_STORAGE;
import static com.jianjian.android.mytimi.tools.Content.REQUEST_CROP;
import static com.jianjian.android.mytimi.tools.Content.REQUEST_SCAN_N;
import static com.jianjian.android.mytimi.tools.Content.author;
import static com.jianjian.android.mytimi.tools.Content.path;

public class photoUtil {

    private File mCurrentFile;
    private Context mContext;

    public Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider
                    .getUriForFile(context.getApplicationContext(), author, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public void crop(Uri inputUri,Context context,String path,int x,int y,int scale){
        Intent intent = new Intent("com.android.camera.action.CROP");

        mCurrentFile = new File(path,System.currentTimeMillis()+".jpg");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(inputUri,"image/*");

        intent.putExtra("crop",true);

        intent.putExtra("aspectX",x);
        intent.putExtra("aspectY",y);

        intent.putExtra("outputX",x*scale);
        intent.putExtra("outputY",y*scale);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection",false);
        intent.putExtra("return-data",false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mCurrentFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());


        ((AppCompatActivity)context).startActivityForResult(intent,REQUEST_CROP);
    }

    public String dealResult(int requestCode, String path, Intent data, ImageView photoImg,int x,int y,int scale){
        String photoPath = null;

        switch (requestCode){
            case REQUEST_CAPTURE:
                crop(getUriForFile(mContext,mCurrentFile),mContext,path,x,y,scale);

                break;
            case REQUEST_SCAN_N:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    File imgUri = new File(GetImagePath.getPath(mContext, data.getData()));
                    Uri dataUri = FileProvider.getUriForFile
                            (mContext, author, imgUri);
                    // Uri dataUri = getImageContentUri(data.getData());
                    crop(dataUri,mContext,path,x,y,scale);
                } else {
                    crop(data.getData(),mContext,path,x,y,scale);
                }

                break;
            case REQUEST_CROP:
                Uri cropUri = FileProvider.getUriForFile(mContext,author,mCurrentFile);
                photoPath = Uri.fromFile(mCurrentFile).toString().substring(7);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(cropUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                photoImg.setImageBitmap(bitmap);
                break;
        }
        return photoPath;
    }

    public void showDialog(Context context){
        mContext = context;
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setItems(mContext.getResources().getStringArray(R.array.photo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File dir = new File(Environment.getExternalStorageDirectory()+"/MyTimi/");
                        if(!dir.exists())
                            dir.mkdir();
                        if (i == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            mCurrentFile = new File(path, System.currentTimeMillis() + ".jpg");
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(mContext,mCurrentFile));
                            ((Activity)mContext).startActivityForResult(intent, REQUEST_CAPTURE);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }
                            ((Activity)mContext).startActivityForResult(intent, REQUEST_SCAN_N);
                        }
                    }

                }).create();
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_STORAGE);
            }else{
                alertDialog.show();
            }
        }else {
            alertDialog.show();
        }
    }
}

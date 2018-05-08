package com.jianjian.android.mytimi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianjian.android.mytimi.R;
import com.jianjian.android.mytimi.tools.Content;
import com.jianjian.android.mytimi.tools.photoUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddRemarkActivity extends AppCompatActivity {


    public static final String INTENT_DATE = "date_intent";
    public static final String RESULT_CONTENT = "result_content";
    public static final String RESULT_PHOTO = "result_photo";

//    public static final int REQUEST_SCAN_PHOTO = 2;
//    public static final int REQUEST_CAPTURE = 3;
//    public static final int REQUEST_SCAN_N = 4;
//    public static final int REQUEST_CROP = 5;
//    public static final int REQUEST_CODE_ASK_STORAGE = 6;
//
//    String path = Environment.getExternalStorageDirectory()+"/myTimi/";


//    String author;

    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.date_text)
    TextView dateText;
    @BindView(R.id.content_edit)
    EditText contentEdit;
    @BindView(R.id.photo_img)
    ImageView photoImg;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;

    Date mDate;
//    File mCurrentFile;

    String photoPath;
    photoUtil mPhotoUtil = new photoUtil();

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,AddRemarkActivity.class);

        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        author = getResources().getString(R.string.author);
        Intent intent = getIntent();
        mDate = (Date)(intent.getSerializableExtra(INTENT_DATE));
        setContentView(R.layout.activity_add_remark);
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

        dateText.setText(simpleDateFormat.format(mDate));
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_CONTENT,contentEdit.getText().toString());
                intent.putExtra(RESULT_PHOTO,photoPath);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        //拍照，选图
        photoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog alertDialog = new AlertDialog.Builder(AddRemarkActivity.this)
//                        .setItems(getResources().getStringArray(R.array.photo), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                File path = new File(Environment.getExternalStorageDirectory()+"/MyTimi/");
//                                if(!path.exists())
//                                    path.mkdir();
//                                if (i == 0) {
//                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    mCurrentFile = new File(path, System.currentTimeMillis() + ".jpg");
//                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                    }
//                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,getUriForFile(AddRemarkActivity.this,mCurrentFile));
//                                    startActivityForResult(intent, REQUEST_CAPTURE);
//                                } else {
//                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                    intent.setType("image/*");
//                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                    }
//                                    startActivityForResult(intent, REQUEST_SCAN_N);
//                                }
//                            }
//
//                        }).create();
//                if (Build.VERSION.SDK_INT >= 23) {
//                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(AddRemarkActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(AddRemarkActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_STORAGE);
//                    }else{
//                        alertDialog.show();
//                    }
//                }else {
//                    alertDialog.show();
//                }
                mPhotoUtil.showDialog(AddRemarkActivity.this);
            }
        });
    }

//    private Uri getUriForFile(Context context, File file) {
//        if (context == null || file == null) {
//            throw new NullPointerException();
//        }
//        Uri uri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = FileProvider
//                    .getUriForFile(context.getApplicationContext(), author, file);
//        } else {
//            uri = Uri.fromFile(file);
//        }
//        return uri;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case REQUEST_CAPTURE:
//                crop(getUriForFile(this,mCurrentFile));
//                break;
//            case REQUEST_SCAN_N:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    File imgUri = new File(GetImagePath.getPath(this, data.getData()));
//                    Uri dataUri = FileProvider.getUriForFile
//                            (this, author, imgUri);
//                    // Uri dataUri = getImageContentUri(data.getData());
//                    crop(dataUri);
//                } else {
//                    crop(data.getData());
//                }
//
//                break;
//            case REQUEST_CROP:
//                Uri cropUri = FileProvider.getUriForFile(this,author,mCurrentFile);
//                photoPath = Uri.fromFile(mCurrentFile).toString().substring(7);
//                Bitmap bitmap = null;
//                try {
//                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropUri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                photoImg.setImageBitmap(bitmap);
//                break;
//        }
        if(resultCode!=RESULT_CANCELED)
            photoPath = mPhotoUtil.dealResult(requestCode, Content.path,data,photoImg,1,1,240);
    }

//    public void crop(Uri inputUri){
//        Intent intent = new Intent("com.android.camera.action.CROP");
//
//        mCurrentFile = new File(path,System.currentTimeMillis()+".jpg");
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
//        intent.setDataAndType(inputUri,"image/*");
//
//        intent.putExtra("crop",true);
//
//        intent.putExtra("aspectX",1);
//        intent.putExtra("aspectY",1);
//
//        intent.putExtra("outputX",240);
//        intent.putExtra("outputY",240);
//
//        intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection",false);
//        intent.putExtra("return-data",false);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mCurrentFile));
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//
//
//        startActivityForResult(intent,REQUEST_CROP);
//    }
}

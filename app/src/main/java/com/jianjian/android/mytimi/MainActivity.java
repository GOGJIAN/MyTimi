package com.jianjian.android.mytimi;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianjian.android.mytimi.activity.AddOrderActivity;
import com.jianjian.android.mytimi.model.Order;
import com.jianjian.android.mytimi.model.OrderLab;
import com.jianjian.android.mytimi.service.PollService;
import com.jianjian.android.mytimi.tools.Content;
import com.jianjian.android.mytimi.tools.DoubleCache;
import com.jianjian.android.mytimi.tools.FileUploader;
import com.jianjian.android.mytimi.tools.ImgDownloader;
import com.jianjian.android.mytimi.tools.MyPreferences;
import com.jianjian.android.mytimi.tools.photoUtil;
import com.jianjian.typecycleview.View.CycleItem;
import com.jianjian.typecycleview.View.MainCycleView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;


import static com.jianjian.android.mytimi.tools.Content.INCOME_TYPE;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.order_recycler_view)
    RecyclerView mOrderRecView;
    @BindView(R.id.add_order_image_button)
    MainCycleView mMainCycleView;
    @BindView(R.id.income_text)
    TextView incomeText;
    @BindView(R.id.pay_text)
    TextView payText;
    @BindView(R.id.imageView)
    ImageView backgroundImg;

    private OrderAdapter mAdapter;
    private OrderLab mOrderLab;

    private String photoName;

    private Integer lastIndexItem;

    DoubleCache mDoubleCache;

    photoUtil mPhotoUtil = new photoUtil();


    private ImgDownloader<ImageView> mImgDownloader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDoubleCache = new DoubleCache();
        ButterKnife.bind(MainActivity.this);
        mOrderLab = OrderLab.get(this);

        PollService.setServiceAlarm(this);

        updateUI();
        mImgDownloader = new ImgDownloader<>(new Handler());
        mImgDownloader.setImgDownloadListener(new ImgDownloader.ImgDownloadListener<ImageView>() {
            @Override
            public void onImgDownLoaded(ImageView target, Bitmap img) {
                mDoubleCache.put(photoName,img);
                target.setImageBitmap(img);
            }
        });
        mImgDownloader.start();
        mImgDownloader.getLooper();
        initView();
    }

    private void initView(){
        mOrderRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mMainCycleView.setData(getItems(mOrderLab.getOrders()));
//        mMainCycleView.setCycleWidth(5);
//        mMainCycleView.setAnimationLength(3000);
//        mMainCycleView.setStartPoint(0);
//        mMainCycleView.setEndPoint(360);
        mMainCycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrder();
            }
        });
        String backPath = MyPreferences.getBackgroundPath(this);

        if(backPath!=null){
            photoName = backPath.substring(backPath.lastIndexOf("/")==-1?0:backPath.lastIndexOf("/")+1);
            Bitmap bitmap = mDoubleCache.get(photoName);
            if(bitmap!=null)
                backgroundImg.setImageBitmap(bitmap);
            else {
//                backgroundImg.setImageBitmap(BitmapFactory.decodeFile(backPath));
                Uri uri = Uri.parse(Content.url+"test")
                        .buildUpon()
                        .appendQueryParameter("filename", photoName)
                        .build();
                mImgDownloader.queueUrl(backgroundImg, uri.toString());
            }
        }
        backgroundImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
//                        .setItems(getResources().getStringArray(R.array.photo), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                File dir = new File(Environment.getExternalStorageDirectory()+"/MyTimi/");
//                                if(!dir.exists())
//                                    dir.mkdir();
//                                if (i == 0) {
//                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    mCurrentFile = new File(path, System.currentTimeMillis() + ".jpg");
//                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                    }
//                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(MainActivity.this,mCurrentFile));
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
//                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_STORAGE);
//                    }else{
//                        alertDialog.show();
//                    }
//                }else {
//                    alertDialog.show();
//                }
                mPhotoUtil.showDialog(MainActivity.this);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImgDownloader.quit();
        mImgDownloader.clearQueue();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case REQUEST_CAPTURE:
//                mCurrentFile = crop(getUriForFile(this,mCurrentFile),this,path);
//                break;
//            case REQUEST_SCAN_N:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    File imgUri = new File(GetImagePath.getPath(this, data.getData()));
//                    Uri dataUri = FileProvider.getUriForFile
//                            (this, author, imgUri);
//                    // Uri dataUri = getImageContentUri(data.getData());
//                    mCurrentFile = crop(dataUri,this,path);
//                } else {
//                    mCurrentFile = crop(data.getData(),this,path);
//                }
//
//                break;
//            case REQUEST_CROP:
//                Uri cropUri = FileProvider.getUriForFile(this,author,mCurrentFile);
//                photoName = Uri.fromFile(mCurrentFile).toString().substring(7);
//                Bitmap bitmap = null;
//                try {
//                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropUri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                backgroundImg.setImageBitmap(bitmap);
//                break;
//        }
        if(resultCode!=RESULT_CANCELED) {
            photoName = mPhotoUtil.dealResult(requestCode, Content.path, data, backgroundImg, 21, 8, 40);
            Bitmap bitmap = BitmapFactory.decodeFile(Content.path+ photoName);
            mDoubleCache.put(photoName,bitmap);
            if(photoName !=null) {
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        File file = new File(photoName);
                        new FileUploader().uploadFile(file);
                    }
                }).start();

            }
            MyPreferences.setBackgroundPath(this, photoName);
        }
    }


    private void getPayAndIncome(){
        List<Order> mOrders = mOrderLab.getOrders();
        float income = 0;
        float pay = 0;
        for(Order o:mOrders){
            if(o.getType()== INCOME_TYPE)
                income+=o.getMoney();
            else
                pay += o.getMoney();
        }
        payText.setText(String.valueOf(pay));
        incomeText.setText(String.valueOf(income));

    }

    public void addOrder(){
//        Random random = new Random();
//        float ff = random.nextFloat()*800;
//        int icon[] = {R.drawable.ic_cake,
//                        R.drawable.ic_card,
//                        R.drawable.ic_drink,
//                        R.drawable.ic_food,
//                        R.drawable.ic_movie,
//                        R.drawable.ic_study};
//        int color[] = {R.color.cakeColor,
//                        R.color.cardColor,
//                        R.color.drinkColor,
//                        R.color.foodColor,
//                        R.color.movieColor,
//                        R.color.studyColor};
//
//        Order order = new Order();
//        order.setType(type);
//        order.setContent("哈哈哈哈");
//        order.setTag("哈哈哈哈");
//        order.setMoney(ff);
//        int i = random.nextInt(6);
//        order.setColor(color[i]);
//        order.setIcon(String.valueOf(icon[i]));
//        mOrderLab.addOrder(order);
//        updateUI();
        Intent intent = new Intent(this,AddOrderActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    //得到item从orders中
    private ArrayList<CycleItem> getItems(List<Order> Orders){
        @SuppressLint("UseSparseArrays")
        HashMap<Integer,Float> itemHash = new HashMap<>();
        ArrayList<CycleItem> items = new ArrayList<>();
        for(Order order:Orders){
            if(itemHash.containsKey(order.getColor())){
                itemHash.put(order.getColor(),itemHash.get(order.getColor())+order.getMoney());
            }else{
                itemHash.put(order.getColor(),order.getMoney());
            }
        }
        for(Integer i:itemHash.keySet()){
            CycleItem item = new CycleItem();
            item.setColor(i);
            item.setNum(itemHash.get(i));
            items.add(item);
        }
        return items;
    }

    private void updateUI(){
        List<Order> Orders = mOrderLab.getOrders();
        //如果Adapter不存在，就创建并添加，如果存在就更新
        if(mAdapter==null) {
            mAdapter = new OrderAdapter(Orders);
            mOrderRecView.setAdapter(mAdapter);
        }else {
            mAdapter.setOrders(Orders);
            mAdapter.notifyDataSetChanged();
        }
        getPayAndIncome();
        mMainCycleView.setData(getItems(Orders));
        mMainCycleView.updateUI();
    }



    private class OrderHolder extends RecyclerView.ViewHolder{

        TextView mTagText;
        TextView mMoneyText;
        TextView mContentText;
        View mBottomLine;
        ImageView mImageView,mEditImg,mDeleteImg,mContentImg;
        FrameLayout mFrameLayout;
        float originalX;
        boolean isFront = true;
        float distance = 400;
        long animationLength = 300;

        public OrderHolder(View itemView) {
            super(itemView);
            mTagText = itemView.findViewById(R.id.tag);
            mMoneyText = itemView.findViewById(R.id.money);
            mContentText = itemView.findViewById(R.id.content);
            mBottomLine = itemView.findViewById(R.id.bottom_line);
            mImageView = itemView.findViewById(R.id.icon);
            mFrameLayout = itemView.findViewById(R.id.text_frame);
            mEditImg = itemView.findViewById(R.id.edit_img);
            mDeleteImg = itemView.findViewById(R.id.delete_img);
            mContentImg = itemView.findViewById(R.id.content_img);
        }

        //初始化组件状态，避免出现RecyclerView复用问题
        public void initViewStatus(){
            mFrameLayout.setVisibility(View.VISIBLE);
            mContentImg.setVisibility(View.VISIBLE);
            mEditImg.setVisibility(View.INVISIBLE);
            mDeleteImg.setVisibility(View.INVISIBLE);
            mEditImg.setEnabled(false);
            mDeleteImg.setEnabled(false);

        }
        public void bindView(final Order order, boolean isLast){
            initViewStatus();
            isFront = true;
            mMoneyText.setText(String.valueOf(order.getMoney()));
            mTagText.setText(order.getTag());
            mContentText.setText(order.getContent());
            mImageView.setImageResource(Integer.parseInt(order.getIcon()));
            //图片下载器
//            mImgDownloader.queueUrl(mContentImg,order.getImage());
            mContentImg.setImageBitmap(BitmapFactory.decodeFile(order.getImage()));
            mBottomLine.setVisibility(isLast?View.INVISIBLE:View.VISIBLE);
            mDeleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOrderLab.removeOrder(order);
                    lastIndexItem = null;
                    updateUI();
                }
            });
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    originalX = mImageView.getX();
                    if(isFront) {
                        if(lastIndexItem!=null){
                            ((OrderHolder)mOrderRecView.findViewHolderForAdapterPosition(lastIndexItem)).setBackAnimation();
                        }
                        setAnimation();
                        lastIndexItem = OrderHolder.this.getAdapterPosition();
                    }
                    else {
                        setBackAnimation();
                        lastIndexItem = null;
                    }
                }
            });
        }



        private void setBackAnimation() {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mEditImg,"X",originalX+distance,originalX).setDuration(animationLength);
            animator.start();
            ObjectAnimator.ofFloat(mDeleteImg,"X",originalX-distance,originalX).setDuration(animationLength).start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mEditImg.setEnabled(false);
                    mDeleteImg.setEnabled(false);
                    mEditImg.setClickable(false);
                    mDeleteImg.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mFrameLayout.setVisibility(View.VISIBLE);
                    mContentImg.setVisibility(View.VISIBLE);
                    mEditImg.setVisibility(View.INVISIBLE);
                    mDeleteImg.setVisibility(View.INVISIBLE);
                    isFront = true;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mFrameLayout.setVisibility(View.VISIBLE);
                    mContentImg.setVisibility(View.VISIBLE);
                    mEditImg.setVisibility(View.INVISIBLE);
                    mDeleteImg.setVisibility(View.INVISIBLE);
                    isFront = true;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

        private void setAnimation(){
            mFrameLayout.setVisibility(View.INVISIBLE);
            mContentImg.setVisibility(View.INVISIBLE);
            mEditImg.setVisibility(View.VISIBLE);
            mDeleteImg.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(mEditImg,"X",originalX,originalX+distance).setDuration(animationLength);
            animator.start();
            ObjectAnimator.ofFloat(mDeleteImg,"X",originalX,originalX-distance).setDuration(animationLength).start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mEditImg.setEnabled(true);
                    mDeleteImg.setEnabled(true);
                    mEditImg.setClickable(true);
                    mDeleteImg.setClickable(true);
                    isFront = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

        }
    }


    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder>{

        private List<Order> mOrders;

        public OrderAdapter(List<Order> list){
            mOrders = list;
        }

        @Override
        public int getItemViewType(int position) {
            return mOrders.get(position).getType();
        }

        //根据不同的viewType返回不同的holder
        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view;
            if(viewType==INCOME_TYPE)
                view = layoutInflater.inflate(R.layout.income_item,parent,false);
            else
                view = layoutInflater.inflate(R.layout.pay_item,parent,false);

            return new OrderHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {
            holder.bindView(mOrders.get(position),position==mOrders.size()-1);
        }

        @Override
        public int getItemCount() {
            return mOrders.size();
        }

        public void setOrders(List<Order> orders) {
            mOrders = orders;
        }
    }
}

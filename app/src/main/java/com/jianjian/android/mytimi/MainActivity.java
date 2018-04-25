package com.jianjian.android.mytimi;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianjian.android.mytimi.View.MainCycleView;
import com.jianjian.android.mytimi.model.Order;
import com.jianjian.android.mytimi.model.OrderLab;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int INCOME_TYPE = 0;
    @BindView(R.id.order_recycler_view)
    RecyclerView mOrderRecView;
    @BindView(R.id.add_order_image_button)
    MainCycleView mMainCycleView;

    private OrderAdapter mAdapter;
    private OrderLab mOrderLab;

    private Integer lastIndexItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        mOrderLab = OrderLab.get(this);
        mOrderRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        initView();
        updateUI();
    }

    private void initView(){
        mMainCycleView.setData(mOrderLab.getOrders());
        mMainCycleView.setCycleWidth(2);
        mMainCycleView.setAnimationLength(1500);
        mMainCycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setItems(getResources().getStringArray(R.array.order_type), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //支出1，收入0
                                switch (i){
                                    case 0:
                                        addOrder(0);
                                        break;
                                    case 1:
                                        addOrder(1);
                                        break;
                                }
                            }
                        }).show();
            }
        });
    }

    public void addOrder(int type){
        Random random = new Random();
        float ff = random.nextFloat()*800;
        int icon[] = {R.drawable.ic_cake,
                        R.drawable.ic_card,
                        R.drawable.ic_drink,
                        R.drawable.ic_food,
                        R.drawable.ic_movie,
                        R.drawable.ic_study};
        int color[] = {R.color.cakeColor,
                        R.color.cardColor,
                        R.color.drinkColor,
                        R.color.foodColor,
                        R.color.movieColor,
                        R.color.studyColor};

        Order order = new Order();
        order.setType(type);
        order.setContent("哈哈哈哈");
        order.setTag("哈哈哈哈");
        order.setMoney(ff);
        int i = random.nextInt(6);
        order.setColor(color[i]);
        order.setIcon(icon[i]);
        mOrderLab.addOrder(order);
        updateUI();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
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
        mMainCycleView.setData(Orders);
        mMainCycleView.updateUI();
    }



    private class OrderHolder extends RecyclerView.ViewHolder{

        TextView mTagText;
        TextView mMoneyText;
        TextView mContentText;
        View mBottomLine;
        ImageView mImageView,mEditImg,mDeleteImg;
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
        }

        //初始化组件状态，避免出现RecyclerView复用问题
        public void initViewStatus(){
            mFrameLayout.setVisibility(View.VISIBLE);
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
            mImageView.setImageResource(order.getIcon());
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
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mFrameLayout.setVisibility(View.VISIBLE);
                    mEditImg.setVisibility(View.INVISIBLE);
                    mDeleteImg.setVisibility(View.INVISIBLE);
                    isFront = true;
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    mFrameLayout.setVisibility(View.VISIBLE);
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

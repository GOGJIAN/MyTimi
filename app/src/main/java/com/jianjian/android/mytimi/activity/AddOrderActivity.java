package com.jianjian.android.mytimi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.jianjian.android.mytimi.R;
import com.jianjian.android.mytimi.model.Order;
import com.jianjian.android.mytimi.model.OrderLab;
import com.jianjian.android.mytimi.model.SelectTagItem;
import com.jianjian.android.mytimi.model.TagLab;
import com.jianjian.android.mytimi.tools.Content;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddOrderActivity extends AppCompatActivity {

    public static final int request_content = 1;

    @BindView(R.id.income_text)
    TextView incomeText;
    @BindView(R.id.pay_text)
    TextView payText;
    @BindView(R.id.selected_img)
    ImageView selectedIcon;
    @BindView(R.id.selected_tag)
    TextView selectedTag;
    @BindView(R.id.money_edit)
    EditText moneyEdit;
    @BindView(R.id.icon_recyclerView)
    RecyclerView iconRecView;

    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.confirm_text)
    TextView confirmTxt;
    @BindView(R.id.add_edit_img)
    ImageButton editBtn;
    @BindView(R.id.date)
    TextView dateText;


    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        ButterKnife.bind(AddOrderActivity.this);
        mOrder = new Order();
        mOrder.setDate(Calendar.getInstance().getTime());
        mOrder.setIcon(TagLab.getInstance().getTags().get(0).getIcon());
        mOrder.setColor(TagLab.getInstance().getTags().get(0).getColor());
        initView();
    }

    private void initView(){
        incomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)view).setTextColor(ContextCompat.getColor(AddOrderActivity.this,R.color.colorPrimary));
                payText.setTextColor(Color.BLACK);
                mOrder.setType(Content.INCOME_TYPE);
            }
        });
        payText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)view).setTextColor(ContextCompat.getColor(AddOrderActivity.this,R.color.colorPrimary));
                incomeText.setTextColor(Color.BLACK);
                mOrder.setType(Content.PAY_TYPE);
            }
        });

        payText.setTextColor(ContextCompat.getColor(AddOrderActivity.this,R.color.colorPrimary));
        incomeText.setTextColor(Color.BLACK);
        mOrder.setType(Content.PAY_TYPE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOrder.setMoney(Float.valueOf(moneyEdit.getText().toString()));
                OrderLab.get(AddOrderActivity.this).addOrder(mOrder);
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddRemarkActivity.getIntent(AddOrderActivity.this);
                intent.putExtra(AddRemarkActivity.INTENT_DATE,mOrder.getDate());
                startActivityForResult(intent,request_content);
            }
        });

        iconRecView.setLayoutManager(new GridLayoutManager(AddOrderActivity.this,5));
        iconRecView.setAdapter(new IconAdapter());
//        iconRecView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
//        iconRecView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePicker datePicker = new DatePicker(AddOrderActivity.this);
                datePicker.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));
                new AlertDialog.Builder(AddOrderActivity.this)
                        .setView(datePicker)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int day = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth();
                                int year = datePicker.getYear();

                                Date date = new GregorianCalendar(year,month,day).getTime();
                                mOrder.setDate(date);
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==request_content&&resultCode==RESULT_OK){
            mOrder.setContent(data.getStringExtra(AddRemarkActivity.RESULT_CONTENT));
            mOrder.setImage(data.getStringExtra(AddRemarkActivity.RESULT_PHOTO));
        }
    }

    private class IconHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mTextView;
        View mView;

        public IconHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.imageView);
            mTextView = (TextView)itemView.findViewById(R.id.textView);
            mView = itemView;
        }
        public void bindView(final SelectTagItem selectItem){

            mImageView.setImageResource(Integer.parseInt(selectItem.getIcon()));
            mTextView.setText(selectItem.getTag());
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIcon.setImageResource(Integer.parseInt(selectItem.getIcon()));
                    selectedTag.setText(selectItem.getTag());
                    mOrder.setIcon(selectItem.getIcon());
                    mOrder.setTag(selectItem.getTag());
                    mOrder.setColor(selectItem.getColor());
                }
            });
        }
    }

    private class IconAdapter extends RecyclerView.Adapter<IconHolder>{

        private ArrayList<SelectTagItem> tags;
        public IconAdapter(){
            tags = TagLab.getInstance().getTags();
        }

        public void setTags(ArrayList<SelectTagItem> tags) {
            this.tags = tags;
        }

        @Override
        public IconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(AddOrderActivity.this);
            View view = inflater.inflate(R.layout.select_item,parent,false);

            return new IconHolder(view);
        }

        @Override
        public void onBindViewHolder(IconHolder holder, int position) {
            holder.bindView(tags.get(position));
        }

        @Override
        public int getItemCount() {
            return tags.size();
        }
    }
}

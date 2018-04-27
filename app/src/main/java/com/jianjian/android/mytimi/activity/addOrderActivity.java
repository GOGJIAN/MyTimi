package com.jianjian.android.mytimi.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianjian.android.mytimi.R;
import com.jianjian.android.mytimi.model.Order;
import com.jianjian.android.mytimi.tools.Content;

import butterknife.BindView;
import butterknife.ButterKnife;

public class addOrderActivity extends AppCompatActivity {

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

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        ButterKnife.bind(addOrderActivity.this);
        mOrder = new Order();
        initView();
    }

    private void initView(){
        incomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)view).setTextColor(getResources().getColor(R.color.colorPrimary));
                payText.setTextColor(Color.BLACK);
                mOrder.setType(Content.INCOME_TYPE);
            }
        });
        payText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)view).setTextColor(getResources().getColor(R.color.colorPrimary));
                incomeText.setTextColor(Color.BLACK);
                mOrder.setType(Content.PAY_TYPE);
            }
        });
        moneyEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                    mOrder.setMoney(Float.valueOf(moneyEdit.getText().toString()));
            }
        });


    }
}

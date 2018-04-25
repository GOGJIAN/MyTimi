package com.jianjian.android.mytimi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianjian.android.mytimi.R;

import butterknife.BindView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
    }
}

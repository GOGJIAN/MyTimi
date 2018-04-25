package com.jianjian.android.mytimi.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by Lenovo on 2017/11/23.
 */

@Entity
public class Order {
    @Id
    private Long id;
    private int type;
    private Date date;
    private String tag;
    private float money;
    private String content;
    private int color;
    private int icon;

    @Generated(hash = 1903197225)
    public Order(Long id, int type, Date date, String tag, float money,
            String content, int color, int icon) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.tag = tag;
        this.money = money;
        this.content = content;
        this.color = color;
        this.icon = icon;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

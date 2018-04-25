package com.jianjian.android.mytimi.model;

import android.content.Context;

import java.util.List;

/**
 * Created by Lenovo on 2017/11/23.
 */

public class OrderLab {

    private static OrderLab sOrderLab;
    private Context mContext;
    private OrderDao mOrderDao;
    private OrderLab(Context context){
        mContext = context;
        initGreenDao();
    }
    private void initGreenDao(){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext.getApplicationContext(),"orderDb",null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        mOrderDao = daoSession.getOrderDao();
    }
    public static OrderLab get(Context context){
        if(sOrderLab==null)
            sOrderLab = new OrderLab(context);
        return sOrderLab;
    }
    public Order getOrder(Long uuid){
        return mOrderDao.queryBuilder().where(OrderDao.Properties.Id.eq(uuid)).build().unique();
    }
    public List<Order> getOrders(){
        return mOrderDao.queryBuilder().list();
    }
    public void addOrder(Order order){
        mOrderDao.insert(order);
    }
    public void removeOrder(Order order){
        mOrderDao.delete(order);
    }
}

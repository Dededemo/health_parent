package com.softeem.dao;

import com.softeem.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);

    //更新可预约人数
    public void editNumberByOrderDate(OrderSetting orderSetting);

    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);

    public long findCountByOrderDate(Date orderDate);

    public List<OrderSetting> getOrderSettingByMonth(String date);

    //根据预约日期查询预约设置信息
    public OrderSetting findByOrderDate(Date orderDate);

}

package com.softeem.dao;

import com.softeem.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByCondition(Order order);

    void add(Order order);

    //@MapKey("id")//加了前端使用orderInfo[1].member这种方式才能有数据
    public Map findById4Detail(Integer id);


    public Integer findOrderCountByDate(String date);

    public Integer findOrderCountAfterDate(String date);

    public Integer findVisitsCountByDate(String date);

    public Integer findVisitsCountAfterDate(String date);

    public List<Map> findHotSetmeal();

}

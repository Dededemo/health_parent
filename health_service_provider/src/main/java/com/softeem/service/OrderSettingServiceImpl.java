package com.softeem.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.softeem.dao.OrderSettingDao;
import com.softeem.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {

        for (OrderSetting orderSetting : list) {
            long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if (count > 0) {
                //大于零表示此数据以存在,我们执行update操作
                orderSettingDao.editNumberByOrderDate(orderSetting);
            } else {
                //否则执行insert操作
                orderSettingDao.add(orderSetting);
            }

        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {

        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(date);
        List<Map> arrayList = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number", orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations", orderSetting.getReservations());//已预约人数
            arrayList.add(orderSettingMap);

        }

        return arrayList;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //大于零表示此数据以存在,我们执行update操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //否则执行insert操作
            orderSettingDao.add(orderSetting);
        }
    }
}

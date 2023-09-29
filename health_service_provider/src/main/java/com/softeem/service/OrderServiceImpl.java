package com.softeem.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.softeem.constant.MessageConstant;
import com.softeem.dao.MemberDao;
import com.softeem.dao.OrderDao;
import com.softeem.dao.OrderSettingDao;
import com.softeem.entity.Result;
import com.softeem.pojo.Member;
import com.softeem.pojo.Order;
import com.softeem.pojo.OrderSetting;
import com.softeem.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
        //1.检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        //把字符串时间转换成时间类型
        Date date = DateUtils.parseString2Date(orderDate);
        //根据用户传入的日期来进行查询
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        //如果orderSetting对象等于null就证明单日不可进行预约
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }


        //2.检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可以预约的人数
        int reservations = orderSetting.getReservations();//可预约人数
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //3.检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        //防止重复预约
        if (member != null) {
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId, date, null, null, setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                //已经完成了预约，不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //4.可以预约，设置预约人数加一
        orderSetting.setReservations(reservations + 1);
        //更新已经预约的人数(+1)
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        if (member == null) {
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        Order order = new Order(member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));

        //添加预约订单
        orderDao.add(order);

        //返回成功信息并且将orderId返回给网页
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findById4Detail(id);
        return map;
    }
}

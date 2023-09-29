package com.softeem.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyun.oss.ClientException;
import com.softeem.constant.MessageConstant;
import com.softeem.constant.RedisMessageConstant;
import com.softeem.entity.Result;
import com.softeem.pojo.Order;
import com.softeem.service.OrderService;
import com.softeem.utils.DateUtils;
import com.softeem.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {


    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约
     *
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        //获取用户提交上来的手机号
        String telephone = (String) map.get("telephone");
        //获取redis里面的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        //redis的验证码为空  或  redis的验证码与用户输入的验证码不一致   都会提示[验证码输入错误]
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //---------------------------以下操作是添加数据了-------------------------------------------
        Result result = null;
        //调用体检预约服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            return new Result(false, "预约失败");
        }

        if (result.isFlag()) {
            //预约成功，发送短信通知
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage("908e94ccf08b4476ba6c876d13f084ad", telephone, "6666");
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
            System.out.println("map = " + map);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);

        }
    }

}

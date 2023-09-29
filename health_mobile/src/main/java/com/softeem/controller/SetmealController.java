package com.softeem.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.softeem.constant.MessageConstant;
import com.softeem.entity.Result;
import com.softeem.pojo.Member;
import com.softeem.pojo.Setmeal;
import com.softeem.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference//(check = false)
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        try {
            List<Setmeal> setmealList = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);

        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findBySetmealId(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
        /*System.out.println("setmeal = " + setmeal);
        List<CheckGroup> checkGroups = setmeal.getCheckGroups();
        for (CheckGroup checkGroup : checkGroups) {
            System.out.println("checkGroup = " + checkGroup);
            List<CheckItem> checkItems = checkGroup.getCheckItems();
            for (CheckItem checkItem : checkItems) {
                System.out.println("checkItem = " + checkItem);
            }
        }*/
    }


    @RequestMapping("/findCookieUser")
    public Result findCookieUser(String cookie) {
        String json = jedisPool.getResource().get(cookie);
        Member member = JSON.parseObject(json, Member.class);
        return new Result(true, "成功", member);
    }

}

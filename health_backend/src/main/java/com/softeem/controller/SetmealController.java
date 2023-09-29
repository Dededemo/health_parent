package com.softeem.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.softeem.constant.MessageConstant;
import com.softeem.constant.RedisConstant;
import com.softeem.entity.PageResult;
import com.softeem.entity.QueryPageBean;
import com.softeem.entity.Result;
import com.softeem.pojo.Setmeal;
import com.softeem.service.SetmealService;
import com.softeem.utils.AliyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;


    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")//权限校验
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {

        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //获得文件名的 . 的下标
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf - 1);
            //拼接出来一个新的文件名
            String filename = System.currentTimeMillis() + suffix;
            //使用AliyunUtils工具类来进行文件上传
            AliyunUtils.upload2aliyun(imgFile.getBytes(), filename);

            //------------------------将图片保存刀redis数据库的setmealPicResources-----------------------------------------
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
            //------------------------将图片保存刀redis数据库的setmealPicResources-----------------------------------------

            //提交成功消息并回传图片名
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    @PreAuthorize("hasAuthority('SETMEAL_ADD')")//权限校验
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.saveOrUpdate(setmeal, checkgroupIds);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }

    }

    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")//权限校验
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.pageQuery(queryPageBean);
        return pageResult;
    }


    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")//权限校验
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }


    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")//权限校验
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAI);
        }

        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    //生成静态页面
    @RequestMapping("/generateHtml")
    public Result generateHtml() {
        try {
            setmealService.generateMobileStaticHtml();
            return new Result(true, "静态网页生成成功");
        } catch (Exception e) {
            return new Result(false, "静态网页生成失败");
        }

    }


}

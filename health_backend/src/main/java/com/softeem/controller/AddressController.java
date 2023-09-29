package com.softeem.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.softeem.constant.MessageConstant;
import com.softeem.constant.RedisConstant;
import com.softeem.entity.PageResult;
import com.softeem.entity.QueryPageBean;
import com.softeem.entity.Result;
import com.softeem.pojo.Address;
import com.softeem.service.AddressService;
import com.softeem.utils.AliyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {


    @Reference
    private AddressService addressService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Address> addressList = addressService.findAll();
            return new Result(true, "", addressList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "地图加载失败");
        }

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = addressService.findPage(queryPageBean);
        return pageResult;
    }

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
            jedisPool.getResource().sadd(RedisConstant.Address_PIC_RESOURCES, filename);
            //------------------------将图片保存刀redis数据库的setmealPicResources-----------------------------------------

            //提交成功消息并回传图片名
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    @RequestMapping("/findUpdate")
    public Result findUpdate(Integer id) {
        try {
            Address address = addressService.findById(id);
            return new Result(true, "查询体检机构成功", address);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询体检机构失败");
        }
    }


    @RequestMapping("/add")
    public Result add(@RequestBody Address address) {
        try {
            addressService.add(address);
            return new Result(true, "体检机构添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "体检机构添加失败");
        }
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            addressService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            return new Result(false, "删除失败");
        }
        return new Result(true, "删除成功");
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Address address) {
        try {
            addressService.edit(address);
            return new Result(true, "体检机构修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "体检机构修改失败");
        }
    }


}

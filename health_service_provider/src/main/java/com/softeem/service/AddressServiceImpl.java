package com.softeem.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.softeem.constant.RedisConstant;
import com.softeem.dao.AddressDao;
import com.softeem.entity.PageResult;
import com.softeem.entity.QueryPageBean;
import com.softeem.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<Address> findAll() {
        return addressDao.findAll();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Address> page = addressDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }


    //将图片名称保存到Redis
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.Address_PIC_DB_RESOURCES, pic);
    }

    @Override
    public void add(Address address) {
        addressDao.add(address);//添加并返回主键
        //将图片名称保存到Redis
        savePic2Redis(address.getImg());//将保存到数据库的图片名保存到redis的数据库中

    }

    @Override
    public void edit(Address address) {
        Address addressById = addressDao.findById(address.getId());
        //如果原图片与新图片名字不一样,证明上传了新图片,就删除原图片
        if (!addressById.getImg().equals(addressById.getImg())) {
            //把原图片删除
            jedisPool.getResource().srem(RedisConstant.Address_PIC_DB_RESOURCES, addressById.getImg());
        }
        //修改
        addressDao.edit(address);
    }

    @Override
    public void delete(Integer id) {
        addressDao.delete(id);
    }

    @Override
    public Address findById(Integer id) {
        Address address = addressDao.findById(id);
        return address;
    }
}

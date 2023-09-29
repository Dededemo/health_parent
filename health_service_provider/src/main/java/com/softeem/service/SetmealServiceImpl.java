package com.softeem.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.softeem.constant.RedisConstant;
import com.softeem.dao.SetmealDao;
import com.softeem.entity.PageResult;
import com.softeem.entity.QueryPageBean;
import com.softeem.pojo.Setmeal;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Value("${out_put_path}")//从属性文件读取输出目录的路径
    private String outputpath;


    //生成静态页面
    public void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐列表静态页面 (一个)
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl", "m_setmeal.html", dataMap);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmeal", this.findBySetmealId(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_" + setmeal.getId() + ".html",
                    dataMap);
        }
    }

    /**
     * 生成html网页的方法
     *
     * @param templateName
     * @param htmlPageName
     * @param dataMap
     */
    public void generateHtml(String templateName, String htmlPageName, Map<String, Object> dataMap) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outputpath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /*------------------------------------------------------------*/


    //将图片名称保存到Redis
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    @Override
    public void saveOrUpdate(Setmeal setmeal, Integer[] checkgroupIds) {

        if (setmeal.getId() != null && setmeal.getId() != 0) {
            Setmeal meal = setmealDao.findById(setmeal.getId());
            //如果原图片与新图片名字不一样,证明上传了新图片,就删除原图片
            if (!meal.getImg().equals(setmeal.getImg())) {
                //把原图片删除
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, meal.getImg());
            }
            //修改
            setmealDao.updateById(setmeal);
            //删除关系表
            setmealDao.deleteAssociation(setmeal.getId());
        } else {
            //添加
            setmealDao.add(setmeal);//添加套餐并返回主键id
        }

        //将图片名称保存到Redis
        savePic2Redis(setmeal.getImg());//将保存到数据库的图片名保存到redis的setmealPicResources数据库中

        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //绑定套餐和检查组的多对多关系
            Map map = new HashMap();
            map.put("setmeal", setmeal);
            map.put("checkgroupIds", checkgroupIds);
            setmealDao.setSetmealAndCheckGroup(map);
        }
        generateMobileStaticHtml();//不管是修改还是添加html都会调用此方法
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealDao.selectByCondition(queryPageBean.getQueryString());
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }


    @Override
    public void delete(Integer id) {
        //删除关系表
        setmealDao.deleteAssociation(id);
        //删除setmeal表
        setmealDao.deleteById(id);
    }

    @Override
    public Map findById(Integer id) {
        List<Integer> checkGroupIds = setmealDao.findCheckgroupIdsBySetmealId(id);
        Setmeal setmeal = setmealDao.findById(id);
        Map map = new HashMap();
        map.put("setmeal", setmeal);
        map.put("checkGroupIds", checkGroupIds);
        return map;
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {

        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();

    }

    /**
     * 此方法要查询套餐信息的同时,查询它的检查组信息且查询检查组下的检查项信息
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findBySetmealId(Integer id) {

        return setmealDao.findBySetmealId(id);
    }


}

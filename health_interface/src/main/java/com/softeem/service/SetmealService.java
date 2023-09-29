package com.softeem.service;


import com.softeem.entity.PageResult;
import com.softeem.entity.QueryPageBean;
import com.softeem.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    public void saveOrUpdate(Setmeal setmeal, Integer[] checkgroupIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void delete(Integer id);

    public Map findById(Integer id);

    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    public List<Setmeal> findAll();

    Setmeal findBySetmealId(Integer id);

    void generateMobileStaticHtml();

    List<Map<String, Object>> findSetmealCount();


}

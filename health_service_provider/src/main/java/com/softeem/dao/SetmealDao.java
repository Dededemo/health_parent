package com.softeem.dao;

import com.github.pagehelper.Page;
import com.softeem.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);

    public void setSetmealAndCheckGroup(@Param("map") Map<String, Integer> map);

    public Page<Setmeal> selectByCondition(@Param("queryString") String queryString);

    public void deleteById(Integer id);

    public void deleteAssociation(Integer id);

    public Setmeal findById(Integer id);

    List<Integer> findCheckgroupIdsBySetmealId(Integer id);

    public void updateById(Setmeal setmeal);

    public List<Setmeal> findAll();

    public Setmeal findBySetmealId(Integer id);

    public List<Map<String, Object>> findSetmealCount();
}

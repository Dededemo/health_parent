package com.softeem.dao;

import com.github.pagehelper.Page;
import com.softeem.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 持久层Dao接口
 */
public interface CheckGroupDao {

    /**
     * 添加检查组的基本信息
     *
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 用于添加检查组与检查项的关联关系
     *
     * @param map
     */
    void setCheckGroupAndCheckItem(Map map);

    // 测试插入
    void setCheckGroupAndCheckItem1(@Param("map") Map map);


    public Page<CheckGroup> selectByCondition(@Param("queryString") String queryString);

    public CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    public void deleteById(Integer id);

    void edit(CheckGroup checkGroup);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupById(@Param("id") Integer setmealId);

}

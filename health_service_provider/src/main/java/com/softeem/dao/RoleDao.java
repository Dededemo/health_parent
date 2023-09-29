package com.softeem.dao;

import com.softeem.pojo.Role;

import java.util.Set;

public interface RoleDao {
    //用户的角色是不可重复的 set不可重复但无序
    public Set<Role> findByUserId(int id);
}

package com.softeem.dao;

import com.softeem.pojo.User;

public interface UserDao {
    public User findByUsername(String username);
}

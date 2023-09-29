package com.softeem.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.softeem.dao.PermissionDao;
import com.softeem.dao.RoleDao;
import com.softeem.dao.UserDao;
import com.softeem.pojo.Permission;
import com.softeem.pojo.Role;
import com.softeem.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUsername(String username) {
        //通过用户名查询此用户信息保存到user对象
        User user = userDao.findByUsername(username);
        if (user != null) {

            //通过用户的主键id查询出此用户的所有角色信息
            Set<Role> roleSet = roleDao.findByUserId(user.getId());
            //将此用户的角色信息保存到user对象中
            user.setRoles(roleSet);
            //循环角色信息,将每个角色的权限查询出来
            if (roleSet != null && roleSet.size() > 0) {

                for (Role role : roleSet) {
                    //通过角色的主键id,将此角色的所有权限查询出来
                    Set<Permission> permissionSet = permissionDao.findByRoleId(role.getId());
                    if (permissionSet != null && permissionSet.size() > 0) {
                        //将此角色的权限保存到此角色role对象中
                        role.setPermissions(permissionSet);
                    }
                }

            }
        }
        return user;
    }
}

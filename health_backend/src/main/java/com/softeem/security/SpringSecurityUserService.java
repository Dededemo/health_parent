package com.softeem.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.softeem.pojo.Permission;
import com.softeem.pojo.Role;
import com.softeem.pojo.User;
import com.softeem.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//等效于    <bean id="userService" class="com.softeem.security.UserService"></bean>
@Component//起名:("userService")
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);
        if (user == null) {
            return null;
        }

        //权限与角色保存到list集合中
        List<GrantedAuthority> list = new ArrayList<>();
        //从用户中获取此用户的所有角色
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //将用户的角色都保存到list集合中
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            //从角色中获取此角色的所有权限
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //将用户的权限保存到此list集合中
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return userDetails;
    }


}

package pers.liuchengyin.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pers.liuchengyin.security.pojo.SecurityUser;
import pers.liuchengyin.security.pojo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserDetailServiceImpl
 * @Description 数据源
 * @Author 柳成荫
 * @Date 2021/3/26
 */
@Service("userDetailsService")
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("根据username去数据库查询用户信息，username:{}", username);
        // 1、从数据库中取出用户信息 - 这里模拟，直接new一个User对象
        User user = new User();
        user.setUsername(username);
        // 111111经过加密后
        user.setPassword("96e79218965eb72c92a549dd5a330112");
        SecurityUser securityUser = new SecurityUser(user);
        // 可以根据查出来的user.getId()去查询这个用户对应的权限集合 - 这里模拟，直接new一个结合
        List<String> authorities = new ArrayList<>();
        // 将权限赋予用户
        securityUser.setPermissionValueList(authorities);
        return securityUser;
    }
}

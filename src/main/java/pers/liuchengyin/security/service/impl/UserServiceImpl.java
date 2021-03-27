package pers.liuchengyin.security.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pers.liuchengyin.security.pojo.Menu;
import pers.liuchengyin.security.pojo.Role;
import pers.liuchengyin.security.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserService
 * @Description
 * @Author 柳成荫
 * @Date 2021/3/26
 */
@Component
public class UserServiceImpl implements UserService {
    @Override
    public List<Menu> getAllMenus() {
        // URL和角色都应该从数据库获取，这里为了方便，写死了
        // 根据我写的逻辑，那些受控制的权限，我们才存数据库
        // 假设就这三个受控制
        Menu menu1 = new Menu(1,"/admin/test");
        Menu menu3 = new Menu(3,"/super/**");
        Menu menu2 = new Menu(2,"/admin/test1");
        List<Menu> list = new ArrayList<>();
        // 角色和权限和menu的关系可以自己设计，一般来说，用户和角色一对多，角色和权限是多对多，权限和menu一对一
        Role role1 = new Role(1,"ROLE_admin","admin玩家");
        Role role2 = new Role(1,"ROLE_superAdmin","superAdmin玩家");
        List<Role> roles = new ArrayList<>();
        List<Role> roles2 = new ArrayList<>();
        roles.add(role1);
        roles2.add(role2);
        // 角色与URL关系
        // 我这里设计的是一个
        // url和角色的关系，一个url可以被那些角色访问
        // menu1和menu3可以被role1（ROLE_admin）访问
        menu1.setRoles(roles);
        menu3.setRoles(roles);
        // menu2可以被role2（ROLE_superAdmin）
        menu2.setRoles(roles2);
        list.add(menu1);
        list.add(menu2);
        list.add(menu3);
        return list;
    }
}

package pers.liuchengyin.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * @ClassName TestController
 * @Description 测试控制器
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@RestController
public class TestController {

    @GetMapping("/super/test")
    public String superTest(){
        return "根据权限控制 - PreAuthorize - superTest";
    }

    @GetMapping("/super/test1")
    public String superTest1(){
        return "根据权限控制 - PreAuthorize - superTest1";
    }

    @GetMapping("/super/test/test")
    public String superTestTest(){
        return "根据权限控制 - PreAuthorize - superTestTest";
    }

    @GetMapping("/admin/test")
//    @PreAuthorize("hasAnyAuthority('admin.test')")
    public String test(){
        return "根据权限控制 - PreAuthorize - test";
    }

    @GetMapping("/admin/test0/{id}")
//    @PreAuthorize("hasAnyAuthority('admin.test')")
    public String test0(@PathVariable("id") String id){
        return "根据权限控制 - PreAuthorize - test0 + " + id;
    }

    @GetMapping("/admin/test00")
    @PreAuthorize("hasAnyAuthority('admin.test')")
    public String test00(@RequestParam("id") String id){
        return "根据权限控制 - PreAuthorize - test00 + " + id;
    }

    @GetMapping("/admin/test1")
//    @RolesAllowed("ROLE_admin")
    public String test1(){
        return "根据角色控制 - RolesAllowed，可省略ROLE_，但这里未省略 -test1";
    }

    @GetMapping("/admin/test2")
    @RolesAllowed("admin")
    public String test2(){
        return "根据角色控制 - RolesAllowed，可省略ROLE_ - test2";
    }

    @GetMapping("/admin/test3")
    @Secured("ROLE_admin")
    public String test3(){
        return "根据角色控制 - Secured，不可省略，需要写完整 - test3";
    }

    @GetMapping("/admin/test4")
    public String test4(){
        return "无控制，登录即可访问！ - test4";
    }

    @GetMapping("/admin/test5")
    @PreAuthorize("hasAnyAuthority('admin.test.no')")
    public String test5(){
        return "根据权限控制 - PreAuthorize - test5";
    }

    @GetMapping("/admin/test6")
    @RolesAllowed("ROLE_superAdmin")
    public String test6(){
        return "根据角色控制 - RolesAllowed，可省略ROLE_，但这里未省略 - test6";
    }

    @GetMapping("/admin/test7")
    @RolesAllowed("superAdmin")
    public String test7(){
        return "根据角色控制 - RolesAllowed，可省略ROLE_ - test7";
    }

    @GetMapping("/admin/test8")
    @Secured("ROLE_superAdmin")
    public String test8(){
        return "根据角色控制 - Secured，不可省略，需要写完整 - test8";
    }
}

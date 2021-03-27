package pers.liuchengyin.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Role
 * @Description
 * @Author 柳成荫
 * @Date 2021/3/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Integer id;
    private String name;
    /** 角色名，如经理、HR */
    private String rname;
}

package pers.liuchengyin.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassName Menu
 * @Description
 * @Author 柳成荫
 * @Date 2021/3/26
 */
@Data
public class Menu {
    private Integer id;
    private String pattern;
    private List<Role> roles;

    public Menu(Integer id, String pattern) {
        this.id = id;
        this.pattern = pattern;
    }
}

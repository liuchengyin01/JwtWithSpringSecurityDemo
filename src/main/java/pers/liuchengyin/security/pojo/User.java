package pers.liuchengyin.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description 用户实体类
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
     /** 用户名 */
    private String username;
     /** 密码 */
    private String password;
     /** 昵称 */
    private String nickName;
     /** 用户头像 */
    private String salt;
     /** 用户签名 */
    private String token;
}


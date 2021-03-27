package pers.liuchengyin.security.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pers.liuchengyin.security.utils.MD5;

/**
 * @ClassName DefaultPasswordEncoder
 * @Description 自定义的密码处理方法类型 - 这里我采用的MD5
 * 如果不用这个，可以直接在TokenWebSecurityConfig里传入BCryptPasswordEncoder来加密解密也可以
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {
    /**
     * 加密
     * @param rawPassword rawPassword
     * @return 加密后的数据
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    /**
     * 匹配
     * @param rawPassword rawPassword
     * @param encodedPassword encodedPassword
     * @return 是否匹配
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
    }
}

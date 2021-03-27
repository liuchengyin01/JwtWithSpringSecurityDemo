package pers.liuchengyin.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName TokenManager
 * @Description Token管理类
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@Component
public class TokenManager {
    /** token过期时间 24小时 */
    private long tokenExpiration = 24 * 60 * 60 * 1000;
    /** 60秒过期时间 */
//    private long tokenExpiration = 60 * 1000;
    /** 签名 - 自己定义 */
    private String tokenSignKey = "liuchengyin";

    /**
     * 创建一个Token，主体为username
     * @param username 用户名
     * @return
     */
    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                // 使用GZIP压缩
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    /**
     * 根据token获取信息
     * @param token token
     * @return
     */
    public String getUserFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenSignKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    /**
     * 判断token是否存在与有效 - 是否超过过期时间
     * @param jwtToken jwtToken
     * @return 是否有效
     */
    public boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)){
            return false;
        }
        try {
            Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     * 前端发给我们，我们要从Header去获取token，判断这个token是否有效
     * @param request
     * @return 是否有效
     */
    public boolean checkToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        return checkToken(jwtToken);
    }

}
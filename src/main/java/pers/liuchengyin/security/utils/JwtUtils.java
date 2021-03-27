package pers.liuchengyin.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName JwtUtils
 * @Description JWT工具类 - 本Demo没有用到
 * @Author 柳成荫
 * @Date 2021/3/25
 */
public class JwtUtils {
    /** JWT过期时间 */
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    /**
     * 密钥 - 随意设置
     * token是根据这个秘钥进行加密/解密的
     */
    public static final String APP_SECRET = "liuchengyin";

    /**
     * 根据id和昵称获取token
     * @param id
     * @param nickname
     * @return
     */
    public static String getJwtToken(String id, String nickname){
        String JwtToken = Jwts.builder()
                // 头部信息 - Header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 负载 - Payload
                .setSubject("lcy-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                // 额外信息
                .claim("id", id)
                .claim("nickname", nickname)
                // 尾部 - 算法、秘钥
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
        return JwtToken;
    }

    /**
     * 判断token是否存在与有效 - 是否超过过期时间
     * @param jwtToken jwtToken
     * @return 是否有效
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)){
            return false;
        }
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
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
    public static boolean checkToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        return checkToken(jwtToken);
    }

    /**
     * 根据token获取id - 这个id就是上面getJwtToken方法里存的id
     * @param request request
     * @return
     */
    public static String getUserIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("id");
    }
}

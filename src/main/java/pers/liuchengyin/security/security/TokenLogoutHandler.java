package pers.liuchengyin.security.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import pers.liuchengyin.security.entity.ResponseResult;
import pers.liuchengyin.security.utils.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName TokenLogoutHandler
 * @Description 登出业务逻辑类
 * @Author 柳成荫
 * @Date 2021/3/25
 */
public class TokenLogoutHandler implements LogoutHandler {
    /** Token管理类 */
    private TokenManager tokenManager;

    public TokenLogoutHandler(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    /**
     * 登出业务处理
     * @param request request
     * @param response response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("X-Token");
        if (token != null) {
            /* tokenManager.removeToken(token); */
            try{
                String userName = tokenManager.getUserFromToken(token);
            }catch (ExpiredJwtException e){
                // 可能token过期了，所以得捕获
                ResponseUtil.out(response, ResponseResult.ok());
            }
            // 清空当前用户缓存中的权限数据
            // 如果你的权限使用userName作为key存在Redis中，可以通过userName将缓存清空
            // ....
        }
        ResponseUtil.out(response, ResponseResult.ok());
    }

}
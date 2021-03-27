package pers.liuchengyin.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pers.liuchengyin.security.entity.ResponseResult;
import pers.liuchengyin.security.pojo.SecurityUser;
import pers.liuchengyin.security.pojo.User;
import pers.liuchengyin.security.security.TokenManager;
import pers.liuchengyin.security.utils.ResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @ClassName TokenLoginFilter 认证过滤器
 * @Description 登录过滤器，继承UsernamePasswordAuthenticationFilter，对用户名密码进行登录校验
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@Slf4j
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private TokenManager tokenManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.setPostOnly(false);
        // 认证路径 - 发送什么请求，就会进行认证
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/service_auth/admin/index/login","POST"));
    }


    /**
     * 尝试认证 - 当发起登录请求，会先来到这个方法
     * @param request request
     * @param response response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            // 也可以直接获取账号密码
            String username = obtainUsername(request);
            String password = obtainPassword(request);
            log.info("TokenLoginFilter-attemptAuthentication：尝试认证，用户名:{}, 密码:{}", username, password);
            // 在authenticate里去进行校验的，校验过程中会去把UserDetailService里返回的SecurityUser(UserDetails)里的账号密码和这里传的账号密码进行比对
            // 并在UserDetailService里将权限进行赋予
            // 校验通过，会进入到successfulAuthentication方法
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 登录成功
     * @param request request
     * @param response response
     * @param chain chain
     * @param auth auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        log.info("TokenLoginFilter-successfulAuthentication：认证通过！");
        SecurityUser user = (SecurityUser) auth.getPrincipal();
        // 创建token
        String token = tokenManager.createToken(user.getCurrentUserInfo().getUsername());
        log.info("创建的Token为：{}", token);
        // 这里建议，以username为Key，权限集合为value将权限存入Redis，因为权限在后面会频繁被取出来用
//        redisTemplate.opsForValue().set(user.getCurrentUserInfo().getUsername(), user.getPermissionValueList());
        // 响应给前端调用处
        ResponseUtil.out(response, ResponseResult.ok().data("token", token));
    }

    /**
     * 登录失败
     * @param request
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {
        log.info("TokenLoginFilter-unsuccessfulAuthentication：认证失败！");
        // 响应给前端调用处
        ResponseUtil.out(response, ResponseResult.error());
    }
}

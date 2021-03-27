package pers.liuchengyin.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import pers.liuchengyin.security.entity.ResponseResult;
import pers.liuchengyin.security.pojo.Menu;
import pers.liuchengyin.security.pojo.Role;
import pers.liuchengyin.security.security.TokenManager;
import pers.liuchengyin.security.service.impl.UserServiceImpl;
import pers.liuchengyin.security.utils.ResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName TokenAuthenticationFilter
 * @Description 授权过滤器
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    private TokenManager tokenManager;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public TokenAuthenticationFilter(AuthenticationManager authManager, TokenManager tokenManager) {
        super(authManager);
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = null;
        try {
            log.info("授权过滤器，验证Token...");
            authentication = getAuthentication(request);
        } catch (ExpiredJwtException e) {
            // 可能token过期
            log.info("异常捕获：{}",e.getMessage());
            ResponseUtil.out(response, ResponseResult.unauthorized());
        }
        if (authentication != null) {
            String url = request.getRequestURI();
            // 设置不设置都行，如果需要用注解来控制权限，则必须设置
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserServiceImpl userService = new UserServiceImpl();
            List<Menu> menuList = userService.getAllMenus();
            // 遍历所有菜单
            for (Menu menu : menuList) {
                // 如果url匹配上了
                if (antPathMatcher.match(menu.getPattern(), url) && menu.getRoles().size() > 0){
                    log.info("URL匹配上了，请求URL:{}，匹配上的URL:{}", url, menu.getPattern());
                    List<String> stringList = new ArrayList<>();
                    for (GrantedAuthority authority : authentication.getAuthorities()) {
                        String authority1 = authority.getAuthority();
                        stringList.add(authority1);
                    }
                    for (Role role : menu.getRoles()) {
                        if (stringList.contains(role.getName())) {
                            log.info("角色匹配，角色为:{}", role.getName());
                            chain.doFilter(request, response);
                            return;
                        }
                    }
                    // 没有权限
                    log.info("URL匹配上了，但无权访问，请求URL:{}，匹配上的URL:{}", url, menu.getPattern());
                    ResponseUtil.out(response, ResponseResult.noPermission());
                    return;
                }
            }
            // url没有匹配上菜单，可以访问
            log.info("URL未匹配上，所有人都可以访问！");
            chain.doFilter(request, response);
        } else {
            // 没有登录
            log.info("用户Token无效！");
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("X-Token");
        log.info("X-Token:{}", token);
        if (token != null && !"".equals(token.trim())) {
            // 根据token获取用户名
            String userName = tokenManager.getUserFromToken(token);
            // 这里可以根据用户名去Redis中取出权限集合
            // 不应该从SecurityContextHolder获取，会出现问题，如果你换一个token(这个token也是有效的)来调用方法，从这里取，这权限还是之前token登录时存进来的(经过我测试)
            // 为什么呢？我的猜测是：因为JWT是无状态的，你没有办法在注销的时候，将SpringSecurity全局对象里的东西清理
            // 如果你先用账号2登录获取一个token2，然后用账号1登录获取一个token1，用token1去调用一次api的时候从SecurityContextHolder获取一次权限，然后用token2去调用一次api获取一次权限，你会发现这个权限居然是token1拥有的(我测试过)
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(userName);
            // 这里直接模拟从Redis中取出权限
            List<String> permissionValueList = new ArrayList<>();
            // 权限 - 为了测试根据权限控制访问权限
            permissionValueList.add("admin.test");
            // 角色 - 为了测试根据角色控制访问权限
            permissionValueList.add("ROLE_admin");
            // 需要将权限转换成SpringSecurity认识的
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for(String permissionValue : permissionValueList) {
                if(StringUtils.isEmpty(permissionValue)) {
                    continue;
                }
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
                authorities.add(authority);
            }
            if (!StringUtils.isEmpty(userName)) {
                log.info("授权过滤器：授权完成！");
                return new UsernamePasswordAuthenticationToken(userName, token, authorities);
            }
            return null;
        }
        return null;
    }
}
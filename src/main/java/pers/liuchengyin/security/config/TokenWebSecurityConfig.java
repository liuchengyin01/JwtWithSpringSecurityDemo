package pers.liuchengyin.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pers.liuchengyin.security.filter.TokenAuthenticationFilter;
import pers.liuchengyin.security.filter.TokenLoginFilter;
import pers.liuchengyin.security.security.DefaultPasswordEncoder;
import pers.liuchengyin.security.security.TokenLogoutHandler;
import pers.liuchengyin.security.security.TokenManager;
import pers.liuchengyin.security.security.UnauthorizedEntryPoint;

/**
 * @ClassName TokenWebSecurityConfig
 * @Description SpringSecurity配置
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // 根据自己的需求开启 - 注解配置方法安全
        prePostEnabled = true,  // 根绝权限控制
        securedEnabled = true,  // 根据角色控制 - 该注解是Spring security提供的
        jsr250Enabled = true    // 根据角色控制 - 该注解是 JSR250 支持的注解形式
)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 自定义查询数据库类
     */
    private UserDetailsService userDetailsService;
    /**
     * Token管理器
     */
    private TokenManager tokenManager;
    /**
     * 密码加密方式
     */
    private DefaultPasswordEncoder defaultPasswordEncoder;

    /**
     * 注入需要的类，可根据自己的需要进行改造
     * @param userDetailsService 自定义查询数据库类
     * @param defaultPasswordEncoder 密码加密方式
     * @param tokenManager Token管理器
     */
    @Autowired
    public TokenWebSecurityConfig(UserDetailsService userDetailsService,
                                  DefaultPasswordEncoder defaultPasswordEncoder,
                                  TokenManager tokenManager) {
        this.userDetailsService = userDetailsService;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.tokenManager = tokenManager;
    }

    /**
     * 配置设置 - 更多配置项见官方文档
     * @param http http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                // 允许跨域
                .and().cors()
                .and().csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                // 退出请求路径
                .and().logout().logoutUrl("/service_auth/admin/index/logout")
                // 退出处理器
                .addLogoutHandler(new TokenLogoutHandler(tokenManager)).and()
                // 认证过滤器
                .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager))
                // 授权过滤器
                .addFilter(new TokenAuthenticationFilter(authenticationManager(), tokenManager)).httpBasic();
    }

    /**
     * 数据源配置、加密方式配置
     * @param builder builder
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 这里如果需要BCryptPassword加密，第二个参数传入passwordEncoder()即可
        builder.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截，这些是没有认证就可以访问的路径
     * @param web web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"
        );
    }

    /**
     * BCryptPassword加密
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

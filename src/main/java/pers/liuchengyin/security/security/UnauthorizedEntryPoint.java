package pers.liuchengyin.security.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import pers.liuchengyin.security.entity.ResponseResult;
import pers.liuchengyin.security.utils.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName UnauthorizedEntryPoint
 * @Description 未授权的统一处理方式
 * @Author 柳成荫
 * @Date 2021/3/25
 */
@Slf4j
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    /**
     * 未授权返回错误码
     * @param request request
     * @param response response
     * @param authException authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 响应错误码
        ResponseUtil.out(response, ResponseResult.error());
    }
}
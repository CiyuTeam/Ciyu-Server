package com.ciyu.app.security;


import com.ciyu.app.exception.UnauthorizedException;
import com.ciyu.app.pojo.User;
import com.ciyu.app.service.token.TokenService;
import com.ciyu.app.service.user.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

@Component @RequiredArgsConstructor @Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        // 获取方法
        Method method = handlerMethod.getMethod();
        // 如果添加了@IgnoreSecurity注解，直接通过
        if (method.isAnnotationPresent(IgnoreSecurity.class)) {
            return true;
        }
        // 获取请求地址
        String requestPath = request.getRequestURI();
        // 白名单
        if (requestPath.contains("/error")) {
            return true;
        }
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue == null || !authorizationHeaderValue.startsWith("Bearer")) {
            throw new UnauthorizedException();
        }
        String token = authorizationHeaderValue.substring(7);
        String phone = tokenService.extractPhone(token);
        User user = userService.findUserByPhone(phone).orElseThrow();
        request.setAttribute("currentUser", user);
        return true;
    }
}

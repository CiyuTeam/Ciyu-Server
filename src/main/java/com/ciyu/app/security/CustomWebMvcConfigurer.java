package com.ciyu.app.security;

import com.ciyu.app.security.AuthInterceptor;
import com.ciyu.app.security.CurrentUserMethodArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(authInterceptor);
        // 配置拦截的路径
        interceptorRegistration.addPathPatterns("/api/v1/**");
        // 配置不拦截的路径
        interceptorRegistration.excludePathPatterns("**/swagger-ui.html");
        interceptorRegistration.excludePathPatterns("**/swagger-ui/index.html");
        interceptorRegistration.excludePathPatterns("**/api-doc.yaml");
        interceptorRegistration.excludePathPatterns("**/api-doc");
        // 还可以在这里注册其它的拦截器
        // registry.addInterceptor(new OtherInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
    }

    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }
}

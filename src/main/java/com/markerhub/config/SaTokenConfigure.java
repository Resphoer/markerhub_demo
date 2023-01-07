package com.markerhub.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    //Sa-Token解决跨域请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaHolder.getResponse()
                    .setHeader("Access-Control-Allow-Origin", "*")
                    .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                    .setHeader("Access-Control-Max-Age", "3600")
                    .setHeader("Access-Control-Allow-Headers", "*");
            SaRouter.match(SaHttpMethod.OPTIONS)
                    .free(r -> System.out.println("-----------OPTIONS预检请求，不做处理"))
                    .back();
        }));
    }

    //解决跨域问题
//
//    public SaServletFilter getSaServletFilter(){
//        return new SaServletFilter()
//                .addInclude("/logout","/blog/**").addExclude("/login","/blogs/**","/js/**","/css/**","/fonts/**","/img/**","/favicon.ico","/index.html")
//                .setAuth(handler -> StpUtil.checkLogin())
//                .setError(Throwable::getMessage)
//                .setBeforeAuth(obj -> {
//                    SaHolder.getResponse()
//                            .setHeader("Access-Control-Allow-Origin", "*")
//                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
//                            .setHeader("Access-Control-Max-Age", "3600")
//                            .setHeader("Access-Control-Allow-Headers", "*");
//                    SaRouter.match(SaHttpMethod.OPTIONS)
//                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
//                            .back();
//                });
//    }

    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt(){
        return new StpLogicJwtForSimple();
    }
}

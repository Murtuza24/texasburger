package com.egen.texasburger.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Murtuza
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    ExecutionTimeInterceptor timeInterceptor;

    @Autowired
    RequestValidatorInterceptor requstValidatorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);


        registry.addInterceptor(requstValidatorInterceptor)
                .addPathPatterns("/api/*");
        //.excludePathPatterns("/menus/*");

    }

}

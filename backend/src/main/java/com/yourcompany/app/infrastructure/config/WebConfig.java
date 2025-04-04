package com.yourcompany.app.infrastructure.config;

import com.yourcompany.app.infrastructure.security.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final AuthenticationFilter authenticationFilter;
    
    public WebConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }
    
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistration() {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authenticationFilter);
        registration.addUrlPatterns("/api/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
package com.yourcompany.app.infrastructure.feature;

import org.ff4j.FF4j;
import org.ff4j.web.FF4jDispatcherServlet;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureFlagConfig {

    @Bean
    public FF4j getFF4j() {
        FF4j ff4j = new FF4j();
        
        // Define default features
        ff4j.createFeature("hello-world-feature", true);
        ff4j.createFeature("payment-processing", true);
        ff4j.createFeature("new-user-onboarding", false);
        
        return ff4j;
    }
    
    @Bean
    public ServletRegistrationBean<FF4jDispatcherServlet> ff4jDispatcherServletRegistrationBean(FF4j ff4j) {
        FF4jDispatcherServlet ff4jConsoleServlet = new FF4jDispatcherServlet();
        ff4jConsoleServlet.setFf4j(ff4j);
        return new ServletRegistrationBean<>(ff4jConsoleServlet, "/ff4j-console/*");
    }
}

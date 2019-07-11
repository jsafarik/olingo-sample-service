package com.jts.trippin.web;

import org.apache.catalina.LifecycleException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws LifecycleException {
        SpringApplication.run(TripPinServlet.class, args);
    }

    @Bean
    public ServletRegistrationBean exampleServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new TripPinServlet(), "/odata.svc/*");
        bean.setLoadOnStartup(1);
        return bean;
    }

}

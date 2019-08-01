package com.jts.trippin.web;

import org.apache.catalina.LifecycleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.jts.trippin.data.model.entityset.entity.Advertisement;
import com.jts.trippin.data.model.entityset.entity.Category;
import com.jts.trippin.data.model.entityset.entity.Product;
import com.jts.trippin.data.model.entityset.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.jts.trippin")
public class Main extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public ServletRegistrationBean exampleServletBean(@Autowired TripPinServlet servlet) {
        ServletRegistrationBean bean = new ServletRegistrationBean(servlet, "/odata.svc/*");
        bean.setLoadOnStartup(1);
        return bean;
    }

    @Bean(name = "odataEntities")
    public List<Class<?>> odataEntities() {
        return new ArrayList<>(Arrays.asList(User.class, Product.class, Category.class, Advertisement.class));
    }

}

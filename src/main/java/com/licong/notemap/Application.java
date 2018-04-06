package com.licong.notemap;

import com.bstek.ureport.console.UReportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by lctm2005 on 2015/6/20.
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@ImportResource("classpath:ureport-console-context.xml")
public class Application extends SpringBootServletInitializer {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean buildUReportServlet() {
        return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");
    }
}

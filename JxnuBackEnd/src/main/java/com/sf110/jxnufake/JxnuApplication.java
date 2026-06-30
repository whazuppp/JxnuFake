package com.sf110.jxnufake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ServletComponentScan //开启对Servlet组件的支持   比如WebFilter
@SpringBootApplication
public class JxnuApplication {

    public static void main(String[] args) {
        SpringApplication.run(JxnuApplication.class, args);
    }

}

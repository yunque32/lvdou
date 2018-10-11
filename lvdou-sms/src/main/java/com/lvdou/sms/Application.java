package com.lvdou.sms;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args){
        System.out.println("===========");
        // 创建SpringApplication对象
        SpringApplication springApplication =
                new SpringApplication(Application.class);
        // 去掉Banner
        springApplication.setBannerMode(Banner.Mode.OFF);
        // 运行
        springApplication.run(args);
    }
}

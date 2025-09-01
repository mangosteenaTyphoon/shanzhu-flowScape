package com.shanzhu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan({"com.shanzhu.**.mapper"})
@ComponentScan({"com.shanzhu.**"})
public class ShanZhuApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShanZhuApplication.class, args);
        System.out.println("启动成功");
    }
}

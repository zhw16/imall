package com.zhang.imall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//指定扫描mapper接口位置，Spring就能知道我们的Mapper接口在哪儿了，也就能去创建Mapper接口对应的对象了
@MapperScan(basePackages = "com.zhang.imall.model.dao")
//在程序入口开启swagger的api
@EnableSwagger2
//开启spring的缓存.我们希望使用缓存的ServiceImpl方法上，使用【@Cacheable(value = "***")】注解：缓存该方法的返回值；
@EnableCaching
//启用定时任务
@EnableScheduling
public class ImallApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImallApplication.class, args);
    }

}

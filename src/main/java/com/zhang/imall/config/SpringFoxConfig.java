package com.zhang.imall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 配置api文档生成的信息
 * 之后创建ImallWebMvcConfig类，去配置地址映射
 */
@Configuration
public class SpringFoxConfig {
    //访问http://localhost:8000/swagger-ui.html可以看到API文档
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //前端api网站对应的名字
                .title("imall商城")
                .description("一个用于学习spring boot的商城项目")
                .termsOfServiceUrl("")
                .build();
    }
}

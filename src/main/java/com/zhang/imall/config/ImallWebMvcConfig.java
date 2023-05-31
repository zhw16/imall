package com.zhang.imall.config;

import com.zhang.imall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置api文档的地址映射
 */
@Configuration//说明这个类是配置类
public class ImallWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        //商品图片等定义静态资源映射
        //http://localhost:8000/images/b483c5f0-c20b-4af4-9fbc-3d67c627d5c1.png，是找不到的，
        // 配置后可以找到D:\Projects\spring-boot-learn\imall\imallImages\里面的图片
        //这样一来，我们在上传图片时候，我们就能用这个为了安全考虑，而和图片实际地址不同的url，访问到图片了；
        //其实，【上传图片】接口返回给前端这个url，然后这个url会作为【增加图片】接口的image参数，
        // 其会被存在数据库中；然后，浏览器在显示商品的时候，就可以从数据库中拿到这个url，然后就能够在浏览器上显示出这个图片了；
        // 但是以“/image/product/开头的请求都被找到本地的目录下”
        registry.addResourceHandler("/images/product/**").addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR_PRODUCT);
        registry.addResourceHandler("/images/pay/**").addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR_PAY);
		//前端资源地址映射
        registry.addResourceHandler("/admin/**"). addResourceLocations("classpath:/static/admin/");
    }
}

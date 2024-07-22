package com.zhang.imall.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName SheduleController
 * @Author ZhangHewen
 * @Description
 * @Date 2023/7/18 11:37
 * @Version V1.0
 **/

//@RestController
@Controller
public class ScheduleController {


    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @ApiOperation("测试定时任务")
    @GetMapping("/schedule/testSchedule")
    @Scheduled(cron = "0 0/10 * * *  ? " )
    public void testScheduleTask() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("执行任务"+simpleDateFormat.format(new Date()));
    }


    @ApiOperation("test01重定向")
    @GetMapping("/test/test01")
    public void testScheduleTask(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://www.baidu.com");
//        response.sendRedirect("zf");
    }

    @ApiOperation("直接返回静态页面")
    @GetMapping("/test/test02")
    public String testScheduleTask1() {
        return ("zf");
    }
}

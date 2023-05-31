package com.zhang.imall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang.imall.common.Constant;
import com.zhang.imall.model.pojo.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * aop统一处理程序的日志
 * web日志的过滤器,面向切面的
 */
@Aspect
@Component
public class WebLogAspect {
    private long startTime;         // 开始时间
    //记录内容
    private final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    //controller.*.*(..)  ----类名.方法名(参数)
    //切入点，指定拦截点
    @Pointcut("execution(public * com.zhang.imall.controller.*.*(..))")
    public void webLog() {

    }

    /**
     * 在webLog（）方法前进行切入
     *
     * @param joinPoint 连接点信息，主要是一些类的信息
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 开始时间
        startTime =System.currentTimeMillis();
        //收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();//得到请求对象
        User currentUser = (User) request.getSession().getAttribute(Constant.IMALL_USER);
        //日志打印url信息
        if (currentUser!=null) {
            log.info("CURRENT_USER:"+currentUser.getUsername()+";");
        }
        //在log文件中打印url的信息
        log.info("URL:" + request.getRequestURL().toString() + ";");
        //打印请求的类型
        log.info("HTTP_METHOD:" + request.getMethod() + ";");
        //拿到IP信息
        log.info("IP:" + request.getRemoteAddr() + ";");
        //getDeclaringTypeName，打印类的一些信息；getName():签名信息；
        log.info("CLASS_INFO:" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + ";");
        //获得参数信息
        log.info("ARGS:" + Arrays.toString(joinPoint.getArgs()) + ";");
    }

    /**
     * 处理完请求，返回对象
     * @param res 返回的内容
     * pointcut 拦截点
     */
    @AfterReturning(returning = "res", pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        //返回的内容
        String s = new ObjectMapper().writeValueAsString(res);
        //处理完请求，返回对象
        log.info("RESPONSE:" + s);
    }

    @After("webLog()")
    public void doAfter() {
        long E_time = System.currentTimeMillis() - startTime;
        log.info("TIME_CONSUMING:" + E_time + "ms");
    }
}

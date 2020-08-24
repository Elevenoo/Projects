package com.example.idbsystem.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Order(1)
@Component
@Slf4j
public class LoggerAspect {
//    @Slf4j 注解为类提供一个静态 org.slf4j.Logger 对象
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoggerAspect.class);
    private final Logger log = LoggerFactory.getLogger(LoggerAspect.class);   //添加Slf4j注解通过lomboc创建Logger对象
    /**
     *  日志类，监控controller层的方法，打印
     *  请求url、ip、参数、方法名、耗时
     */

    /**
     *  定义切入点：监听所有controller层方法
     */
    @Pointcut("execution(* com.example.idbsystem.controller..*.*(..))")
    public void loggerpointcut() {}


    @Around("loggerpointcut()")
    public Object controllerLog(ProceedingJoinPoint pdj) throws Throwable{

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        long startTime = System.currentTimeMillis();//开始时间

        Object[] args = pdj.getArgs();    //获取参数数组
        Object ret = pdj.proceed(args);   //执行原方法并获取返回结果

        long endTime = System.currentTimeMillis();//结束时间

        // 记录下请求内容
        StringBuilder sb = new StringBuilder();
        sb.append("  URL: " + request.getRequestURL().toString());
        sb.append("  IP: " + request.getRemoteAddr());
        sb.append("  Params: " + Arrays.toString(args));
        sb.append("  CLASS_METHOD: " + pdj.getSignature().getDeclaringTypeName() + "." + pdj.getSignature().getName());
        sb.append("  Time: " + (endTime - startTime) + "毫秒");

        log.info(sb.toString());
        return ret;
    }


}

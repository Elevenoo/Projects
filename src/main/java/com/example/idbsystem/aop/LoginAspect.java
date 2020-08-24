package com.example.idbsystem.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Aspect
@Order(2)
@Component
public class LoginAspect {

//    @Resource   //import javax.annotation.Resource;
    boolean isLogin=false;

    /**
     *  定义切入点
     *  避免未登陆通过指定的URL请求进入网站的情况
     */

    @Pointcut("execution(public * com.example.idbsystem.controller..*.*(..))"+
            "&& !execution(public * com.example.idbsystem.controller..*.net(..))"+
            "&& !execution(public * com.example.idbsystem.controller..*.login(..))"+
            "&& !execution(public * com.example.idbsystem.controller..*.resetPassword(..))"+
            "&& !execution(public * com.example.idbsystem.controller.ResposeController.*(..))")
    public void loginedPointcut() {}

    //在一个指定URL请求执行之前要判断当前是否存于登录状态
    @Around("loginedPointcut()")
    public Object loged(ProceedingJoinPoint pdj) throws Throwable {
        Object result;
//        if(isLogin) {
            Object[] args = pdj.getArgs();    //获取参数数组
            result= pdj.proceed(args);   //执行原方法并获取返回结果
//        }else{
//            System.out.println("请先登录");
//            result="/login";
//        }
        return result;
    }

    /**
     *  是否登录
     */
    @Pointcut("execution(public * com.example.idbsystem.controller..*.login(..))"+
            "|| execution(public * com.example.idbsystem.controller..*.net(..))")
    public void logingPointcut() {}

    //登录和退出的请求执行完后要记得更新登陆的状态
//    @After(pointcut = "logingPointcut()")  //由于登录方法可能没有成功登录仍停留在登录界面，所以不能通过后置通知（After），而需要通过AfterReturning来判断状态
    @AfterReturning(pointcut = "logingPointcut()",returning = "result")
    public void afterreturningloging(JoinPoint joinPoint, Object result){
        String str = (String) result;
        if(str.equals("/home")) isLogin=true;
        else isLogin=false;
    }

}
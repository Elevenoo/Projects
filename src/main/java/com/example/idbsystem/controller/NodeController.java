package com.example.idbsystem.controller;

import com.example.idbsystem.bean.User;
import com.example.idbsystem.service.lmpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
public class NodeController {

    @Resource
    UserServiceImpl usi;

    @RequestMapping("/806db.net")
    public String net(){
        return "/login";
    }

    @RequestMapping(value = "/806db.login",method = RequestMethod.POST)
    public String login(HttpServletRequest request){
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        String result;
        if(usi.selectuser(user))
            result="/home";
        else
            result="/login";
        return result;
    }

    @RequestMapping("/806db.resetPassword")
    public String resetPassword(){
        return "/resetpw";
    }


    @RequestMapping("/806db.home")
    public String home(){
        return "/home";
    }

    @RequestMapping("/806db.uploadimginfors")
    public String uploadimginfors(){
        return "/uploadimginfors";
    }

    @RequestMapping("/806db.history")
    public String history(){
        return "/history";
    }
}

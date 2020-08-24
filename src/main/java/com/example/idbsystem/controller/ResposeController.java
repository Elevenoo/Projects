package com.example.idbsystem.controller;

import com.example.idbsystem.bean.ImgInfos;
import com.example.idbsystem.bean.User;
import com.example.idbsystem.service.UserServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Controller
public class ResposeController {
    @Resource
    UserServiceImpl usi;

    //需要在这个方法上添加事务
    @Transactional
    @RequestMapping(value = "/806db.reportImginfos",method = RequestMethod.POST)
    public String reportImginfos(HttpServletRequest request){
        ImgInfos imgInfos = new ImgInfos();
        imgInfos.setPid(Integer.valueOf(request.getParameter("pid")));
        imgInfos.setExamtime(request.getParameter("examtime"));
        imgInfos.setDiagnosis(request.getParameter("diagnosis"));
        imgInfos.setAge(Float.valueOf(request.getParameter("age")));
        imgInfos.setSex(request.getParameter("sex"));
        imgInfos.setSex(request.getParameter("sex"));
        imgInfos.setEdu(Integer.valueOf(request.getParameter("edu")));
        imgInfos.setMmse(Integer.valueOf(request.getParameter("mmse")));
        imgInfos.setMoca(Integer.valueOf(request.getParameter("moca")));
        byte[] uploadfmriimg = new byte[0];
        byte[] uploadsmriimg=new byte[0];
        byte[] uploadfdgpetimg=new byte[0];
        byte[] uploadabetapetimg=new byte[0];
        if(!request.getParameter("uploadfmri").isEmpty()) {
            uploadfmriimg = usi.getImg(request.getParameter("uploadfmri"));
            imgInfos.setFmri("1");
        }
        if(!request.getParameter("uploadsmri").isEmpty()) {
            uploadsmriimg = usi.getImg(request.getParameter("uploadsmri"));
            imgInfos.setSmri("1");
        }
        if(!request.getParameter("uploadfdgpet").isEmpty()) {
            uploadfdgpetimg = usi.getImg(request.getParameter("uploadfdgpet"));
            imgInfos.setAbetapet("1");
        }
        if(!request.getParameter("uploadabetapet").isEmpty()) {
            uploadabetapetimg = usi.getImg(request.getParameter("uploadabetapet"));
            imgInfos.setFdgpet("1");
        }

        usi.insertImginros(imgInfos);
        if(!request.getParameter("uploadfmri").isEmpty()) usi.insertfmri(imgInfos.getPid(), imgInfos.getExamtime(), uploadfmriimg);
        if(!request.getParameter("uploadsmri").isEmpty()) usi.insertsmri(imgInfos.getPid(), imgInfos.getExamtime(), uploadsmriimg);
        if(!request.getParameter("uploadfdgpet").isEmpty()) usi.insertabetapet(imgInfos.getPid(), imgInfos.getExamtime(), uploadfdgpetimg);
        if(!request.getParameter("uploadabetapet").isEmpty()) usi.insertfdgpet(imgInfos.getPid(), imgInfos.getExamtime(), uploadabetapetimg);

        return "/uploadimginfors";
    }


    @RequestMapping(value = "/806db.batchdownload", method = RequestMethod.POST)
    @ResponseBody
    public boolean batchdownload(HttpServletRequest request){
        String arr[] = request.getParameterValues("arr[]");
        if(arr==null) return false;
        try {
            //        String zipname=usi.setSaveAs();
            String zipname="C:\\Users\\ASUS\\Desktop\\down.zip";
            String xlsname="C:\\Users\\ASUS\\Desktop\\report.xls";
            List<ImgInfos> imgInfoslist=new LinkedList<>();
            OutputStream outputStream = new FileOutputStream(new File(zipname));
            ZipOutputStream zoStream  = new ZipOutputStream(outputStream);
            for (String s : arr) {
                s = s.replaceAll("'", "");
                int m1 = s.indexOf(',');
                int m2 = s.lastIndexOf(',');
                String table = s.substring(m2 + 1);
                String pid = s.substring(0, m1);
                String examtime = s.substring(m1 + 1, m2);
//                System.out.println(table + "  " + pid + "  " + examtime);
                usi.loadImg(table, pid, examtime,outputStream,zoStream);
                List<ImgInfos> imgInfosl = usi.selectImginros(pid,examtime);
                imgInfoslist.addAll(new LinkedList<>(imgInfosl));
            }
            zoStream.close();
            outputStream.close();
            //下载execl
            usi.toexcel(xlsname,imgInfoslist);
            System.out.println("下载完成");
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }


    @RequestMapping(value = "/806db.download", method = RequestMethod.POST)
    @ResponseBody
    public boolean download(HttpServletRequest request){
        String table=request.getParameter("mode");
        String pid = request.getParameter("pid");
        String examtime = request.getParameter("examtime");
//        String zipname=usi.setSaveAs();
        String zipname="C:\\Users\\ASUS\\Desktop\\down.zip";
        String xlsname="C:\\Users\\ASUS\\Desktop\\report.xls";
        List<ImgInfos> imgInfoslist = usi.selectImginros(pid,examtime);
        try {
            // 下载影像
            OutputStream outputStream = new FileOutputStream(new File(zipname));
            ZipOutputStream zoStream  = new ZipOutputStream(outputStream);
            usi.loadImg(table, pid, examtime,outputStream,zoStream);
            zoStream.close();
            outputStream.close();
            //下载execl
            usi.toexcel(xlsname,imgInfoslist);
            System.out.println("下载完成");
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping(value = "/806db.resetPW", method = RequestMethod.POST)
    public String resetPW(HttpServletRequest request){

        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        //先确认该用户身份
        if(!usi.selectuser(user))
            return "/resetPW";
        //确定新密码和确认密码一致
        if(!request.getParameter("newpassword").equals(request.getParameter("newpasswordcheck")))
            return "/resetPW";
        user.setPassword(request.getParameter("newpassword"));
        if(usi.updateuser(user))
            return "/home";
        else
            return "/resetpw";
    }


    @RequestMapping(value = "/806db.getimginfors",method =  RequestMethod.POST)
    @ResponseBody
    public PageInfo<ImgInfos> getImginfors(HttpServletRequest request){
        int pageNum = Integer.valueOf(request.getParameter("pageNum"));
        String diagnosis = request.getParameter("diagnosis");
        String fmri = request.getParameter("fmri");
        String smri = request.getParameter("smri");
        String abetapet = request.getParameter("abetapet");
        String fdgpet = request.getParameter("fdgpet");
        System.out.println(pageNum+" "+diagnosis+" "+fmri+" "+smri+" "+abetapet+" "+fdgpet);

        PageHelper.startPage(pageNum,5);
        List<ImgInfos> list = usi.selectImginros("All", "0", "0", "0", "0");
        PageInfo<ImgInfos> pageInfo = new PageInfo<ImgInfos>(list);
        System.out.println("<-------查询结果------------");
        System.out.println(pageInfo);
        System.out.println("--------------------------->");
        return pageInfo;
    }


}

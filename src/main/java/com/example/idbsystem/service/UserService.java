package com.example.idbsystem.service;

import com.example.idbsystem.bean.ImgInfos;
import com.example.idbsystem.bean.User;

import java.util.List;

public interface UserService {

    boolean updateuser(User user);

    boolean selectuser(User user);

    String selectByName(String name);

    boolean insertImginros(ImgInfos imgInfos);

    boolean insertfmri(Integer pid, String examtime, byte[] img);

    boolean insertsmri(Integer pid, String examtime, byte[] img);

    boolean insertabetapet(Integer pid, String examtime, byte[] img);

    boolean insertfdgpet(Integer pid, String examtime, byte[] img);

    List<ImgInfos> selectImginros(String diagnosis, String fmri, String smri, String abetapet, String fdgpet);

    List<ImgInfos> selectImginros(String pid, String examtime);

    String selectimg(String table, String pid, String examtime);
}

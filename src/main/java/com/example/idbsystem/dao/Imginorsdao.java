package com.example.idbsystem.dao;

import com.example.idbsystem.bean.ImgInfos;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface Imginorsdao {

    @Insert("insert manage(pid,examtime,diagnosis,age,sex,edu,mmse,moca,fmri,smri,abetapet,fdgpet)values(#{pid},#{examtime},#{diagnosis},#{age},#{sex},#{edu},#{mmse},#{moca},#{fmri},#{smri},#{abetapet},#{fdgpet})")
    boolean insertImginros(ImgInfos imgInfos);

    @Insert("insert fmri(pid,examtime,img)values(#{pid},#{examtime},#{img})")
    boolean insertfmri(Integer pid, String examtime, byte[] img);

    @Insert("insert smri(pid,examtime,img)values(#{pid},#{examtime},#{img})")
    boolean insertsmri(Integer pid, String examtime, byte[] img);

    @Insert("insert abetapet(pid,examtime,img)values(#{pid},#{examtime},#{img})")
    boolean insertabetapet(Integer pid, String examtime, byte[] img);

    @Insert("insert fdgpet(pid,examtime,img)values(#{pid},#{examtime},#{img})")
    boolean insertfdgpet(Integer pid, String examtime, byte[] img);

    /**
     * 动态查询
     * 每个操作的注解还提供一个provider注解，可以通过java方法提供动态sql语句
     * method参数指定的方法，必须是public String的，可以为static。
     */
//    @Select("select * from manage where diagnosis=#{diagnosis} and fmri=#{fmri} and smri=#{smri} and abetapet=#{abetapet} and fdgpet=#{fdgpet}")
    @SelectProvider(type = com.example.idbsystem.dao.SqlProvider.class,method = "selectImginrosSql")
    List<ImgInfos> selectImginros(String diagnosis, String fmri, String smri, String abetapet, String fdgpet);

    @Select("select * from manage where pid=#{pid} and examtime=#{examtime}")
    List<ImgInfos> selectImginrosbyidandexamtime(String pid, String examtime);


    /**
     * #{}和 ${}注入参数，需要注意单引号的问题
     * @Param 设置key, 可以通过key取值
     */
//    @Select("select img from ${param1} where pid='${param2}' and examtime='${param3}'")
//    String selectimg(String table, String pid, String examtime);
    @Select("select img from ${table} where pid=#{pid} and examtime=#{examtime}")
    String selectimg(@Param("table") String table, @Param("pid") String pid, @Param("examtime") String examtime);

}

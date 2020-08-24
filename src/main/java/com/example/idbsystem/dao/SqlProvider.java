package com.example.idbsystem.dao;

public class SqlProvider {

    public String selectImginrosSql(String diagnosis, String fmri, String smri, String abetapet, String fdgpet){

        String strsql="select * from manage where";
        if(!diagnosis.equals("All"))
            strsql+=" diagnosis='"+diagnosis+"'";
        if(fmri.equals("1"))
            strsql+=" and fmri='1'";
        if(smri.equals("1"))
            strsql+=" and smri='1'";
        if(abetapet.equals("1"))
            strsql+=" and abetapet='1'";
        if(fdgpet.equals("1"))
            strsql+=" and fdgpet='1'";

        //校正sql语句
        if(strsql.endsWith("where"))
            strsql=strsql.replace("where","");
        if(strsql.indexOf("where and")>0)
            strsql=strsql.replace("where and","where");
        return strsql;
    }
}

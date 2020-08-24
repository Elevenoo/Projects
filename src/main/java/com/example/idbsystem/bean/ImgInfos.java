package com.example.idbsystem.bean;

import java.io.Serializable;

public class ImgInfos implements Serializable {
    private Integer pid;
    private String examtime;
    private String diagnosis="NC";
    private Float age;
    private String sex="男";
    private Integer edu;
    private Integer mmse;
    private Integer moca;
    private String fmri="0";
    private String smri="0";
    private String abetapet="0";
    private String fdgpet="0";

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getExamtime() {
        return examtime;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Float getAge() {
        return age;
    }

    public void setAge(Float age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getEdu() {
        return edu;
    }

    public void setEdu(Integer edu) {
        this.edu = edu;
    }

    public Integer getMmse() {
        return mmse;
    }

    public void setMmse(Integer mmse) {
        this.mmse = mmse;
    }

    public Integer getMoca() {
        return moca;
    }

    public void setMoca(Integer moca) {
        this.moca = moca;
    }

    public String getFmri() {
        return fmri;
    }

    public void setFmri(String fmri) {
        this.fmri = fmri;
    }

    public String getSmri() {
        return smri;
    }

    public void setSmri(String smri) {
        this.smri = smri;
    }

    public String getAbetapet() {
        return abetapet;
    }

    public void setAbetapet(String abetapet) {
        this.abetapet = abetapet;
    }

    public String getFdgpet() {
        return fdgpet;
    }

    public void setFdgpet(String fdgpet) {
        this.fdgpet = fdgpet;
    }


}

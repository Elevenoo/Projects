package com.example.idbsystem.service.lmpl;

import com.example.idbsystem.bean.ImgInfos;
import com.example.idbsystem.bean.User;
import com.example.idbsystem.dao.Imginorsdao;
import com.example.idbsystem.dao.Userdao;
import com.example.idbsystem.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    Userdao ud;
    @Resource
    Imginorsdao id;


    @Override
    public boolean updateuser(User user) {
        return ud.updateuser(user);
    }

    @Override
    public boolean selectuser(User user) {
        return ud.selectuser(user)!=null;
    }

    @Override
    public String selectByName(String name) {
        return ud.selectByName(name);
    }


    @Override
    public boolean insertImginros(ImgInfos imgInfos) {
        return id.insertImginros(imgInfos);
    }

    @Override
    public boolean insertfmri(Integer pid, String examtime, byte[] img) {
        return id.insertfmri( pid,  examtime,  img);
    }

    @Override
    public boolean insertsmri(Integer pid, String examtime, byte[] img) {
        return id.insertsmri( pid,  examtime,  img);
    }

    @Override
    public boolean insertabetapet(Integer pid, String examtime, byte[] img) {
        return id.insertabetapet( pid,  examtime,  img);
    }

    @Override
    public boolean insertfdgpet(Integer pid, String examtime, byte[] img) {
        return id.insertfdgpet( pid,  examtime,  img);
    }

    @Override
    public List<ImgInfos> selectImginros(String diagnosis, String fmri, String smri, String abetapet, String fdgpet) {
        return id.selectImginros(diagnosis, fmri,smri,abetapet,fdgpet);
    }


    @Override
    public List<ImgInfos> selectImginros(String pid, String examtime) {
        return id.selectImginrosbyidandexamtime(pid,examtime);
    }


    @Override
    public String selectimg(String table, String pid, String examtime){
        return id.selectimg(table, pid, examtime);
    }

    public byte[] getImg(String filepath){
        ByteArrayOutputStream baso=new ByteArrayOutputStream();
        try {
            //获取文件输入流
            FileInputStream input = new FileInputStream(filepath);
            //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input));
            //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
            ZipEntry ze = null;
            //循环遍历
            while ((ze = zipInputStream.getNextEntry()) != null) {
                System.out.println("文件名：" + ze.getName() + " 文件大小：" + ze.getSize() + " bytes");
                System.out.println("文件内容：");
                BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream, Charset.forName("GBK")));  //, Charset.forName("GBK")
                String line;
                while((line=br.readLine())!=null)
                    baso.write(line.getBytes());
            }
            //一定记得关闭流
            zipInputStream.closeEntry();
            input.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return baso.toByteArray();
    }

    public void loadImg(String table, String pid, String examtime,OutputStream outputStream,ZipOutputStream zoStream){
        String str = selectimg(table,pid,examtime);
        String filename = pid+"_"+examtime+"_"+table+".docx";
        try {
            byte[] imgdata=str.getBytes();
//            OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\ASUS\\Desktop\\down.zip"));
//            ZipOutputStream zoStream  = new ZipOutputStream(outputStream);

            zoStream.putNextEntry(new ZipEntry(filename));

            int len;
            ByteArrayInputStream in = new ByteArrayInputStream(imgdata);
            byte[] buf = new byte[BUFFER_SIZE];
            while ((len = in.read(buf)) != -1) {
                zoStream.write(buf, 0, len);
            }

            zoStream.closeEntry();
//            zoStream.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String setSaveAs() {
        JFileChooser fileChooser = new JFileChooser("E:\\");

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showOpenDialog(fileChooser);

        String absolutePath=null;
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
        }
        System.out.println(absolutePath);
        return absolutePath;
    }

    /**
     * 写入Excel
     */
    public void toexcel(String xlsname, List<ImgInfos> imgInfosList){

        //定义表头
        String[] title = {"PID","ExamTime","Diagnosis","Age","Sex","Educational years","MMSE","MoCA","fMRI","sMRI","AV45 PET","FDG PET"};
        //创建excel工作簿
        // 第一步，创建一个workbook，对应一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();

        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet hssfSheet = workbook.createSheet("sheet1");

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short

        HSSFRow row = hssfSheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();

        //居中样式
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFCell hssfCell = null;

        //写入首行标题
        for (int i = 0; i < title.length; i++) {
            hssfCell = row.createCell(i);//列索引从0开始
            hssfCell.setCellValue(title[i]);//列名1
            hssfCell.setCellStyle(hssfCellStyle);//列居中显示
        }

        //写入行记录

        for (int i = 0; i < imgInfosList.size(); i++) {
            //行
            row = hssfSheet.createRow(i+1);
            ImgInfos imgInfos = imgInfosList.get(i);

            // 列设置值
            row.createCell(0).setCellValue(imgInfos.getPid());
            row.createCell(1).setCellValue(imgInfos.getExamtime());
            row.createCell(2).setCellValue(imgInfos.getDiagnosis());
            row.createCell(3).setCellValue(imgInfos.getAge());
            row.createCell(4).setCellValue(imgInfos.getSex());
            row.createCell(5).setCellValue(imgInfos.getEdu());
            row.createCell(6).setCellValue(imgInfos.getMmse());
            row.createCell(7).setCellValue(imgInfos.getMoca());
            row.createCell(8).setCellValue(imgInfos.getFmri());
            row.createCell(9).setCellValue(imgInfos.getSmri());
            row.createCell(10).setCellValue(imgInfos.getAbetapet());
            row.createCell(11).setCellValue(imgInfos.getFdgpet());

        }

        //输出Excel文件
        try {
            FileOutputStream output = new FileOutputStream(xlsname);
            workbook.write(output);
            output.flush();
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

package com.lvdou.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.net.ssl.SSLServerSocket;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public String getImgFileFileName() {
        return imgFileFileName;
    }

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public String getImgFileContentType() {
        return imgFileContentType;
    }

    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }

    private File imgFile;
    private String imgFileFileName;
    private String imgFileContentType;

    @PostMapping("/uploadExcelFile")
    public List<Map<String,String>> selectExcelFile(
            @RequestParam("file")MultipartFile multipartFile){
        List<Map<String,String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("msg","500");
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String subname = FilenameUtils.getExtension(originalFilename);
            StringBuffer url=new StringBuffer("127.0.0.1:9999/admin/index.html?=");
            File file = new File("D://" + originalFilename);
            multipartFile.transferTo(file);
            FileInputStream fis=new FileInputStream(file);
            if("xls".equals(subname)){
                HSSFWorkbook workbook=new HSSFWorkbook(fis);
                HSSFSheet sheet = workbook.getSheet("sheet1");
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    for (Cell cell : row) {
                        if (cell.getCellType() == 1) {
                            int i = cell.getColumnIndex();
                            String value = cell.getStringCellValue();
                            if(i==1){
                                map.put("productName",value);
                            }else if(i==3){
                                map.put("productWeight",value);
                            }else if(i==5){
                                map.put("packing",value);
                            }else if(i==6){
                                map.put("pickPlace",value);
                            }

                        } else if (cell.getCellType() == 0) {
                            int i = cell.getColumnIndex();
                            if (i == 1) {
                                Date d = cell.getDateCellValue();
                                String format = new SimpleDateFormat("yyyy-MM-dd").format(d);
                            } else {
                                double value = cell.getNumericCellValue();
                                String[] a = (value + "").split("\\.");
                            }
                        }
                    }
                    list.add(map);
                }
            } else if("xlxs".equals(subname)){
                XSSFWorkbook wb = new XSSFWorkbook(fis);
                XSSFSheet sheet = wb.getSheet("sheet1");
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    for (Cell cell : row) {
                        if (cell.getCellType() == 1) {
                            int i = cell.getColumnIndex();
                            String value = cell.getStringCellValue();
                            if(i==1){
                                map.put("productName",value);
                            }else if(i==3){
                                map.put("productWeight",value);
                            }else if(i==5){
                                map.put("packing",value);
                            }else if(i==6){
                                map.put("pickPlace",value);
                            }
                        } else if (cell.getCellType() == 0) {
                            int i = cell.getColumnIndex();
                            if (i == 1) {
                                Date d = cell.getDateCellValue();
                                String format = new SimpleDateFormat("yyyy-MM-dd").format(d);
                                System.out.println(format);
                            } else {
                                double value = cell.getNumericCellValue();
                                String[] a = (value + "").split("\\.");
                                System.out.println(a[0]);
                            }
                        }
                    }
                    list.add(map);
                }
            }else{
                System.out.println("上传文件不是Excel文档");
                map.put("msg","上传的文件不是Excel文档");
                return list;
            }
        }catch (IOException e){
            map.put("msg","读取文件异常，请检查文件名");
            e.printStackTrace();
        }catch (Exception e){
            map.put("msg","其他异常");
            e.printStackTrace();
        }
        list.add(map);
        return list;
    }

    @PostMapping("/uploadFile")
    public  Map<String,Object> uploadFile(@RequestParam("file")
             MultipartFile multipartFile ){

        System.out.println("进入到了Controller");
        Map<String, Object> map = new HashMap<>();
        if(multipartFile==null){
            map.put("msg","参数没有传送成功！");
            return map;
        }
        try{
            String originalFilename = multipartFile.getOriginalFilename();
            File file = new File("D://" + originalFilename);
            multipartFile.transferTo(file);
            map.put("msg","成功！");
        } catch (FileNotFoundException e) {
            System.out.println("文件没找到，请检查路径设置！");
            map.put("msg","系统错误：找不到位置存放文件！");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("系统错误：读取不了文件，请检查文件格式是否正确！");
            map.put("msg","系统错误：读取不了文件，请检查文件格式是否正确！");
            e.printStackTrace();
        }
        return map;
    }

}

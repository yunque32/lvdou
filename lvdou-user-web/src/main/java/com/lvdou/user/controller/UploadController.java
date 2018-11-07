package com.lvdou.user.controller;

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

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/upload")
public class UploadController {

    /** 文件服务器的访问地址*/
    @Value("${fileServerUrl}")
    private String fileServerUrl;

    /** 文件上传 */
    @PostMapping("/image")
    public Map<String, Object> upload(@RequestParam("image") MultipartFile multipartFile){
        // {status : 200, url : ''}
        Map<String, Object> data = new HashMap<>();
        data.put("status", 500);
        try{

            String originalFilename = multipartFile.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            File file = new File("D://" + originalFilename);
            System.out.println(file);
            multipartFile.transferTo(file);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return data;
    }
    @PostMapping("/uploadExcelFile")
    public List<Map<String,String>> selectFile(@RequestParam("file")MultipartFile multipartFile){
        List<Map<String,String>> list = new ArrayList<>();
        Map<String, String> statusmap = new HashMap<>();
        statusmap.put("status","500");
        String originalFilename = multipartFile.getOriginalFilename();
        String subname = FilenameUtils.getExtension(originalFilename);
        StringBuffer url=new StringBuffer("127.0.0.1:9101/admin/index.html?=");
        File file = new File("D://" + originalFilename);
        try {
            multipartFile.transferTo(file);
            Map<String, String> map = new HashMap<>();
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
                return list;
            }
        } catch (Exception e) {
            System.out.println("失败了");
            e.printStackTrace();
            list.add(statusmap);
            return list;

        }
        statusmap.put("status","200");
        list.add(statusmap);
        return list;
    };

}

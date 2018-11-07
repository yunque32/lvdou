package com.lvdou.shop.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    /** 文件服务器的访问地址*/
    @Value("${fileServerUrl}")
    private String fileServerUrl;

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
    Map<Object, Object> result = new HashMap<>();
    /** 文件上传 */
    @PostMapping("/file")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile multipartFile){
        // {status : 200, url : ''}
        Map<String, Object> data = new HashMap<>();
        data.put("status", 500);
        try{

            /** 上传文件到FastDFS文件服务器 */
            // 加载配置文件，得到配置文件的绝对路径
            String conf_filename = this.getClass()
                    .getResource("/fastdfs_client.conf").getPath();
            // 初始化客户端全局信息对象
            ClientGlobal.init(conf_filename);
            // 创建存储客户端对象
            StorageClient storageClient = new StorageClient();
            // 获取上传文件的原文件名
            String originalFilename = multipartFile.getOriginalFilename();
            // 上传文件
            /**
             * http://192.168.12.131/group1/M00/00/01/wKgMg1tMHUGALO2fAAMGxOgwmic727.jpg
             * [group1, M00/00/01/wKgMg1tMHB6AdUVgAAMGxOgwmic562.jpg]
             * 数组中第一个元素：组名
             * 数组中第二个元素: 远程文件名称
             */
            String[] arr = storageClient.upload_file(multipartFile.getBytes(),
                    FilenameUtils.getExtension(originalFilename), null);

            // 定义StringBuilder拼接字符串
            StringBuilder url = new StringBuilder(fileServerUrl);
            for (String str : arr){
                url.append("/" + str);
            }

            data.put("status", 200);
            data.put("url", url.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return data;
    }
    @PostMapping("/image")
    public Map<String,Object> uploadImage(@RequestParam("file") MultipartFile multipartFile){
        try {
			/*System.out.println(imgFile);
			System.out.println(imgFileFileName);
			System.out.println(imgFileContentType);*/

            //把图片文件保存到服务器目录（网站的根目录的upload目录）
           // 获取网站下面某个目录的绝对路径： ServletContext.getRealPath("/目录")
            String uploadPath = ServletActionContext.getServletContext().getRealPath("/upload");
            System.out.println(uploadPath);

            //随机生成文件名，防止文件名称重复覆盖问题
            String uuid = UUID.randomUUID().toString();
            //获取文件后缀名： .jpg
            String extName = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
            String fileName = uuid+extName;

            //commons-io -> FileUtils 用于文件的处理
            FileUtils.copyFile(imgFile, new File(uploadPath+"/"+fileName));

            //成功
            result.put("error", 0);
            //文件上传后的地址（url地址）
            //ServletActionContext.getRequest().getContextPath()
            //${pageContext.request.contextPath}
            result.put("url", ServletActionContext.getRequest().getContextPath()+"/upload/"+fileName);
        } catch (Exception e) {
            e.printStackTrace();
//            //失败
            result.put("error", 1);
            result.put("message", e.getMessage());
        }finally{
            //writeJson(result);
        }
    //}
        return null;
    }
}

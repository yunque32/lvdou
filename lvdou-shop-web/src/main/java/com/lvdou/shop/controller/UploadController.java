package com.lvdou.shop.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2018-07-16<p>
 */
@RestController
public class UploadController {

    /** 文件服务器的访问地址*/
    @Value("${fileServerUrl}")
    private String fileServerUrl;

    /** 文件上传 */
    @PostMapping("/upload")
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
}

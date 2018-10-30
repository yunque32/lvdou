package com.lvdou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.user.service.ContentService;
import com.lvdou.pojo.Content;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 广告内容控制器
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference(timeout = 10000)
    private ContentService contentService;

    /** 根据广告分类id查询广告内容 */
    @GetMapping("/findContentByCategoryId")
    public List<Content> findContentByCategoryId(Long categoryId){
        try{
            return contentService.findContentByCategoryId(categoryId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}

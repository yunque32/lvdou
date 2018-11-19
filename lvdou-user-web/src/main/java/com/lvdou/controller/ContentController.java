package com.lvdou.controller;

import com.lvdou.service.ContentServiceImpl;
import com.lvdou.pojo.Content;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/content")
public class ContentController {

    ContentServiceImpl contentService;

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

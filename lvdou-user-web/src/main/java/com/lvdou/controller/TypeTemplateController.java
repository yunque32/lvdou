package com.lvdou.controller;

import com.lvdou.pojo.TypeTemplate;
import com.lvdou.service.impl.TypeTemplateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 类型模版控制器
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Autowired
    private TypeTemplateServiceImpl typeTemplateService;

    /** 根据主键id查询类型模版 */
    @GetMapping("/findOne")
    public TypeTemplate findOne(Long id){
        return typeTemplateService.findOne(id);
    }

    /** 根据类型模版id查询规格与规格选项 */
    @GetMapping("/findSpecByTemplateId")
    public List<Map> findSpecByTemplateId(Long id){
        //  [{"id":27,"text":"网络", "options" : [{},{}]},
        //  {"id":32,"text":"机身内存","options":[{},{}]}]
        return typeTemplateService.findSpecByTemplateId(id);
    }
}

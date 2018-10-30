package com.lvdou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.user.service.ItemSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 商品搜索的控制器
 */
@RestController
public class ItemSearchController {

    @Reference(timeout = 30000)
    private ItemSearchService itemSearchService;

    /** 搜索方法 */
    @PostMapping("/Search")
    public Map<String,Object> search(@RequestBody Map<String, Object> params){
        // 调用服务层方法
        return itemSearchService.search(params);
    }
}

package com.lvdou.controller;

import com.lvdou.service.ItemSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 商品搜索的控制器
 */
@RestController
public class ItemSearchController {

    private ItemSearchServiceImpl itemSearchService;

    /** 搜索方法 */
    @PostMapping("/Search")
    public Map<String,Object> search(@RequestBody Map<String, Object> params){
        // 调用服务层方法
        return itemSearchService.search(params);
    }
}

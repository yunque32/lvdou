package com.lvdou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.pojo.Wuliu;
import com.lvdou..service.WuliuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wuliu")
public class WuliuController {


    @Reference(timeout = 10000)
    private WuliuService wuliuService;

    @GetMapping("/querywuliu")
    public  Wuliu queryWuliu(Long id){

        return wuliuService.queryWuliuById(id);
    }
}

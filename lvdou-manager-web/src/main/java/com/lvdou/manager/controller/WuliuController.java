package com.lvdou.manager.controller;

import com.lvdou.pojo.Wuliu;
import com.lvdou.service.impl.WuliuServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wuliu")
public class WuliuController {


    private WuliuServiceImpl wuliuService;

    @GetMapping("/querywuliu")
    public  Wuliu queryWuliu(Long id){

        return wuliuService.queryWuliuById(id);
    }
}

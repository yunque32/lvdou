package com.lvdou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.pojo.SeckillGoods;
import com.lvdou.seckill.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 秒杀商品控制器
 */
@RestController
@RequestMapping("/seckill")
public class SeckillGoodsController {

    /** 注入秒杀商品服务接口代理对象 */
    @Reference(timeout = 30000)
    private SeckillGoodsService seckillGoodsService;

    /** 查询秒杀商品 */
    @GetMapping("/findSeckillGoods")
    public List<SeckillGoods> findSeckillGoods(){
        // 调用服务
        return seckillGoodsService.findSeckillGoods();
    }

    /** 根据秒杀商品id查询商品 */
    @GetMapping("/findOne")
    public SeckillGoods findOne(Long id){
        return seckillGoodsService.findOneFromRedis(id);
    }
}

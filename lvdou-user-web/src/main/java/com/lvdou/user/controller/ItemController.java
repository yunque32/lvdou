package com.lvdou.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.sellergoods.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class ItemController {

    @Reference(timeout=10000)
    private GoodsService goodsService;

    /**
     * 根据主键id获取商品(SPU、SKU、商品描述)
     * http://item.lvdou.com/149187842867997.html
     * @PathVariable: 取请求URL里面的数据
     * @RequestParam: 取请求参数(post|get)
     * */
//    @GetMapping("/{goodsId}")
//    public String getGoods(@PathVariable("goodsId")Long goodsId, Model model){
//        // 调用服务层
//        Map<String,Object> dataModel = goodsService.getItem(goodsId);
//        // model：数据模型
//        model.addAllAttributes(dataModel);
//        // 返回模版视图item.ftl
//        return "item";
//    }
}

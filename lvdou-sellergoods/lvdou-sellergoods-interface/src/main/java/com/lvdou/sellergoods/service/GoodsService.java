package com.lvdou.sellergoods.service;


import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.Goods;
import com.lvdou.pojo.Item;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午1:52:54
 * @version 1.0
 */
public interface GoodsService {

    /** 保存商品 */
    void saveGoods(Goods goods);

    /** 多条件分页查询商品 */
    PageResult findGoodsByPage(Goods goods, Integer page, Integer rows);

    /** 根据主键id查询商品 */
    Goods findOne(Long id);

    /** 修改商品(SPU、商品描述、SKU) */
    void updateGoods(Goods goods);

    /** 修改商品状态码 */
    void updateAuditStatus(Long[] ids, String status);

    /** 修改商品删除状态 */
    void updateDeleteStatus(Long[] ids);
    /** 根据SPU的id查询SKU数据 */
    List<Item> findItemByGoodsIdAndStatus(Long[] ids, String status);

    /** 根据goodsId查询商品信息 */
    Map<String,Object> getItem(Long goodsId);
}

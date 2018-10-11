package com.lvdou.search.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lvdou.pojo.Item;
import com.lvdou.search.service.ItemSearchService;
import com.lvdou.sellergoods.service.GoodsService;
import com.lvdou.solr.SolrItem;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品消息监听器类
 */
public class ItemMessageListener implements SessionAwareMessageListener<MapMessage>{

    @Reference(timeout = 10000)
    private GoodsService goodsService;
    @Reference(timeout = 30000)
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(MapMessage mapMessage, Session session) throws JMSException {
        System.out.println("======ItemMessageListener=======");
        /** 接收消息内容 */
        List<Long> ids = (List<Long>)mapMessage.getObject("ids");
        String status = mapMessage.getString("status");
        System.out.println("ids: " + ids);
        System.out.println("status: " + status);


        // 根据SPU的id查询SKU数据
        List<Item> itemList = goodsService.findItemByGoodsIdAndStatus(
                ids.toArray(new Long[ids.size()]),status);
        // 把List<Item> 转化成 List<SolrItem>
        List<SolrItem> solrItemList = new ArrayList<>();

        for (Item item1 : itemList){
            // 把item1  转化成 SolrItem
            SolrItem solrItem = new SolrItem();
            solrItem.setId(item1.getId());
            solrItem.setBrand(item1.getBrand());
            solrItem.setCategory(item1.getCategory());
            solrItem.setGoodsId(item1.getGoodsId());
            solrItem.setImage(item1.getImage());
            solrItem.setPrice(item1.getPrice());
            solrItem.setSeller(item1.getSeller());
            solrItem.setTitle(item1.getTitle());
            solrItem.setUpdateTime(item1.getUpdateTime());
            // {"网络":"联通4G","机身内存":"128G"}
            Map<String, String> specMap = JSON.parseObject(item1.getSpec(), Map.class);
            // 动态域
            solrItem.setSpecMap(specMap);
            solrItemList.add(solrItem);
        }

        // 把审核通过的SKU商品同步到Solr索引库
        itemSearchService.saveOrUpdate(solrItemList);
    }
}

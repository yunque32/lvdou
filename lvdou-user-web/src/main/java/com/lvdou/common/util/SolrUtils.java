package com.lvdou.common.util;
import com.alibaba.fastjson.JSON;
import com.lvdou.mapper.ItemMapper;
import com.lvdou.pojo.Item;
import com.lvdou.solr.SolrItem;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolrUtils {

    @Autowired
    private ItemMapper itemMapper;

    private SolrTemplate solrTemplate;

    /** 查询数据库tb_item表中的数据，再写入Solr索引库 */
    public void importDataSolr(){

        // 创建item对象
        Item item = new Item();
        // 可用的SKU商品数据
        item.setStatus("1");
        // 条件查询
        List<Item> itemList = itemMapper.select(item);
        System.out.println("=====商品列表======");
        // 创建集合封装索引库的数据
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
            solrItem.setSpecMap(specMap);

            solrItemList.add(solrItem);
        }
        // 添加或修改
        UpdateResponse updateResponse = solrTemplate.saveBeans(solrItemList);
        // 判断状态码 0:成功
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else{
            solrTemplate.rollback();
        }
        System.out.println("=======结束======");
    }

    public static void main(String[] args){
        // 获取Spring容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 获取SolrUtils
        SolrUtils solrUtils = ac.getBean(SolrUtils.class);
        solrUtils.importDataSolr();
    }
}

package com.lvdou.search.service;

import com.lvdou.solr.SolrItem;

import java.util.List;
import java.util.Map; /**
 * 搜索服务接口
 */
public interface ItemSearchService {

    /** 搜索方法 */
    Map<String,Object> search(Map<String, Object> params);

    /** 添加或修改SKU商品的Solr索引库 */
    void saveOrUpdate(List<SolrItem> solrItemList);

    /** 根据spu的id删除sku商品数据的索引 */
    void delete(List<Long> ids);
}

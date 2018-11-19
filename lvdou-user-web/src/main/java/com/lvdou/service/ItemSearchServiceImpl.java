package com.lvdou.service;

import com.lvdou.solr.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索服务接口实现
 */
// 不需要加事务，因为我们访问的是索引库
public class ItemSearchServiceImpl     {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    /** 添加或修改SKU商品的Solr索引库 */
    public void saveOrUpdate(List<SolrItem> solrItemList){
        // 添加或修改索引库
        UpdateResponse updateResponse = solrTemplate.saveBeans(solrItemList);
        // 判断状态码
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else{
            solrTemplate.rollback();
        }
    }

    /** 根据spu的id删除sku商品数据的索引 */
    public void delete(List<Long> ids){
        // 创建查询对象
        Query query = new SimpleQuery();
        // 条件对象 goodsId in(?,?,?);
        Criteria criteria = new Criteria("goodsId").in(ids);
        // 添加删除条件
        query.addCriteria(criteria);
        // 删除索引
        UpdateResponse updateResponse = solrTemplate.delete(query);
        // 判断状态码
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else{
            solrTemplate.rollback();
        }
    }


    /** 搜索方法 */
    public Map<String,Object> search(Map<String, Object> params){
        try{
            // 定义Map集合封装，最终返回的数据
            Map<String,Object> data = new HashMap<>();
            // 获取关键字
            String keywords = (String)params.get("keywords");

            // 获取当前页码
            Integer page = (Integer) params.get("page");
            if (page == null || page < 1){
                page = 1;
            }
            // 获取每页显示的记录数
            Integer rows = (Integer) params.get("rows");
            if (rows == null){
                rows = 20;
            }

            // 判断关键字
            if (StringUtils.isNoneBlank(keywords)){ // 高亮查询
                // 去掉关键字中间的的空隔
                keywords = keywords.replace(" ","");
                // 创建高亮查询对象
                HighlightQuery highlightQuery = new SimpleHighlightQuery();
                // 创建查询条件 keywords:keywords
                Criteria criteria = new Criteria("keywords").is(keywords);
                // 添加查询条件
                highlightQuery.addCriteria(criteria);
                // 创建高亮选项对象
                HighlightOptions highlightOptions = new HighlightOptions();
                // 设置高亮的Field
                highlightOptions.addField("title");
                // 设置高亮格式器前缀
                highlightOptions.setSimplePrefix("<font color='red'>");
                // 设置高亮格式器后缀
                highlightOptions.setSimplePostfix("</font>");

                // 设置高亮选项
                highlightQuery.setHighlightOptions(highlightOptions);


                // 按商品分类过滤
                String categoryName = (String)params.get("category");
                if (StringUtils.isNoneBlank(categoryName)) {
                    // 定义查询条件
                    Criteria criteria1 = new Criteria("category").is(categoryName);
                    // 添加过滤查询
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                }

                // 按商品品牌过滤
                String brandName = (String)params.get("brand");
                if (StringUtils.isNoneBlank(brandName)) {
                    // 定义查询条件
                    Criteria criteria1 = new Criteria("brand").is(brandName);
                    // 添加过滤查询
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                }

                // 按商品规格选项过滤
                Map<String,String> specMap = (Map<String,String>)params.get("spec");
                if (specMap != null && specMap.size() > 0) {
                    //  "spec_网络": "移动4G",
                    //  "spec_机身内存": "64G",
                    for (String key : specMap.keySet()) {
                        // 定义查询条件
                        Criteria criteria1 = new Criteria("spec_" + key).is(specMap.get(key));
                        // 添加过滤查询
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }
                }
                // 按价格区间过滤
                String price = (String)params.get("price");
                if (StringUtils.isNoneBlank(price)){
                    // 0-500, 500-1000, 3000-*
                    // 得到价格数组
                    String[] priceArr = price.split("-");

                    // 判断数组中第一个元素不等于0
                    if (!"0".equals(priceArr[0])){
                        // 定义查询条件 price >= 500
                        Criteria criteria1 = new Criteria("price").greaterThanEqual(priceArr[0]);
                        // 添加过滤查询
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }

                    // 判断数组中第二个元素不等于星号
                    if (!"*".equals(priceArr[1])){
                        // 定义查询条件 price <= 1000
                        Criteria criteria1 = new Criteria("price").lessThanEqual(priceArr[1]);
                        // 添加过滤查询
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }
                }
                // 增加排序
                // 获取排序Field
                String sortField = (String)params.get("sortField");
                // 获取排序sort(ASC|DESC)
                String sortValue = (String)params.get("sort");
                if (StringUtils.isNoneBlank(sortField) && StringUtils.isNoneBlank(sortValue)) {
                    // 创建排序对象
                    // 第一个参数：Sort.Direction.ASC|Sort.Direction.DESC
                    Sort sort = new Sort("ASC".equalsIgnoreCase(sortValue)
                            ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
                    highlightQuery.addSort(sort);
                }


                // 设置分页开始记录数
                highlightQuery.setOffset((page - 1) * rows);
                // 设置每页显示的记录数
                highlightQuery.setRows(rows);

                // 高亮分页查询，得到高亮分页对象
                HighlightPage<SolrItem> highlightPage = solrTemplate
                        .queryForHighlightPage(highlightQuery, SolrItem.class);
                // 迭代高亮项集合
                for (HighlightEntry<SolrItem> he : highlightPage.getHighlighted()){
                    // 获取原封装实体
                    SolrItem solrItem = he.getEntity();
                    // 判断高亮集合大小(封装多个Field的高亮内容)
                    if (he.getHighlights().size() > 0){
                        // 取第一个高亮Field高亮内容，再取第一个值
                        String title = he.getHighlights().get(0).getSnipplets().get(0);
                        System.out.println("高亮内容：" + title);
                        // 设置标题为高亮后的内容
                        solrItem.setTitle(title);
                    }
                }

                /** 根据关键字分组查询商品分类 */
                List<String> categoryList =  searchCategoryList(keywords);

                // 判断用户是否选择了商品分类
                if (StringUtils.isNoneBlank(categoryName)){
                    // 根据商品分类名称，查询品牌与规格选项
                    Map<String, Object> brandSpecMap = searchBrandAndSpecList(categoryName);
                    // 设置品牌与规格选项
                    data.putAll(brandSpecMap);
                }else {
                    // 判断商品分类集合
                    if (categoryList.size() > 0) {
                        // 取第一个商品分类，查询它对应的品牌与规格
                        categoryName = categoryList.get(0);
                        // 根据商品分类名称，查询品牌与规格选项
                        Map<String, Object> brandSpecMap = searchBrandAndSpecList(categoryName);
                        // 设置品牌与规格选项
                        data.putAll(brandSpecMap);
                    }
                }

                // 设置商品分类
                data.put("categoryList", categoryList);
                // 获取检索的结果
                data.put("rows", highlightPage.getContent());

                // 设置总页数
                data.put("totalPages", highlightPage.getTotalPages());
                // 设置总记录数
                data.put("total", highlightPage.getTotalElements());

            }else{ // 简单查询
                // 创建简单查询对象
                SimpleQuery query = new SimpleQuery("*:*");

                // 设置分页开始记录数
                query.setOffset((page - 1) * rows);
                // 设置每页显示的记录数
                query.setRows(rows);

                /** 分页查询，得到分页对象 */
                ScoredPage scoredPage = solrTemplate.queryForPage(query, SolrItem.class);
                // 获取检索的结果
                data.put("rows", scoredPage.getContent());
                // 设置总页数
                data.put("totalPages", scoredPage.getTotalPages());
                // 设置总记录数
                data.put("total", scoredPage.getTotalElements());
            }

            return data;

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 根据商品分类名称，查询品牌与规格选项 */
    private Map<String,Object> searchBrandAndSpecList(String categoryName) {
        Map<String, Object> brandSpecMap = new HashMap<>();
        // 根据商品分类名称从Redis中查询类型模版id
        Object typeId = redisTemplate.boundHashOps("itemCats").get(categoryName);

        // 根据类型模版id从Redis中查询品牌
        List<Map> brandList = (List<Map>)redisTemplate.boundHashOps("brandList").get(typeId);
        brandSpecMap.put("brandList", brandList);

        // 根据类型模版id从Redis中查询规格选项
        List<Map> specList = (List<Map>)redisTemplate.boundHashOps("specList").get(typeId);
        brandSpecMap.put("specList", specList);

        return brandSpecMap;
    }

    /** 根据关键字分组查询商品分类 */
    private List<String> searchCategoryList(String keywords) {

        // 定义List集合封装商品分类
        List<String> categoryList = new ArrayList<>();
        // 创建简单查询对象
        SimpleQuery simpleQuery = new SimpleQuery("*:*");
        // 创建查询条件 keywords:小米
        Criteria criteria = new Criteria("keywords").is(keywords);
        // 添加查询条件
        simpleQuery.addCriteria(criteria);

        // 创建分组选项，添加分组Field
        GroupOptions groupOptions = new GroupOptions().addGroupByField("category");
        // 设置分组选项
        simpleQuery.setGroupOptions(groupOptions);

        // 分组分页查询，得到分组分页对象
        GroupPage<SolrItem> groupPage = solrTemplate
                .queryForGroupPage(simpleQuery, SolrItem.class);
        // 获取Field获取分组结果集
        GroupResult<SolrItem> groupResult = groupPage.getGroupResult("category");
        // 获取分组项内容集合
        // groupResult.getGroupEntries()： 分组项的分页对象
        List<GroupEntry<SolrItem>> groupEntryList =  groupResult.getGroupEntries().getContent();
        for (GroupEntry<SolrItem> groupEntry : groupEntryList){
            // 获取分组的内容
            System.out.println("商品分类：" + groupEntry.getGroupValue());
            categoryList.add(groupEntry.getGroupValue());
        }

        return categoryList;
    }
}

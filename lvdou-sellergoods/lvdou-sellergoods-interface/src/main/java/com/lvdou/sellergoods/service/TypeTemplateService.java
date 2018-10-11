package com.lvdou.sellergoods.service;

import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.TypeTemplate;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2017年12月7日 下午1:54:30
 * @version 1.0
 */
public interface TypeTemplateService {

    /**
     * 分页查询类型模版
     * @param typeTemplate 模版实体
     * @param page 当前页码
     * @param rows 每页显示的记录数
     * @return PageResult
     */
    PageResult findByPage(TypeTemplate typeTemplate,
                          Integer page, Integer rows);

    /** 添加类型模版 */
    void saveTypeTemplate(TypeTemplate typeTemplate);

    /** 修改类型模版 */
    void updateTypeTemplate(TypeTemplate typeTemplate);

    /** 删除规格与规格选项 */
    void deleteTypeTemplate(Long[] ids);

    /** 根据主键id查询类型模版 */
    TypeTemplate findOne(Long id);

    /** 根据类型模版id查询规格与规格选项 */
    List<Map> findSpecByTemplateId(Long id);

    /** 把类型模版中的品牌与规格选项数据存入Redis */
    void saveToRedis();
}

package com.lvdou.content.service;

import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.ContentCategory;

import java.io.Serializable;
import java.util.List;
/**
 * ContentCategoryService 服务接口
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2018-07-20 08:52:09
 * @version 1.0
 */
public interface ContentCategoryService {

	/** 添加方法 */
	void save(ContentCategory contentCategory);

	/** 修改方法 */
	void update(ContentCategory contentCategory);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	ContentCategory findOne(Serializable id);

	/** 查询全部 */
	List<ContentCategory> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(ContentCategory contentCategory, int page, int rows);

}
package com.lvdou.content.service;

import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.Content;
import java.util.List;
import java.io.Serializable;
/**
 * ContentService 服务接口
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2018-07-20 08:52:09
 * @version 1.0
 */
public interface ContentService {

	/** 添加方法 */
	void save(Content content);

	/** 修改方法 */
	void update(Content content);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Content findOne(Serializable id);

	/** 查询全部 */
	List<Content> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(Content content, int page, int rows);

	/** 根据广告分类id查询广告内容 */
    List<Content> findContentByCategoryId(Long categoryId);
}
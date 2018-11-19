package com.lvdou.service;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.mapper.ContentCategoryMapper;
import com.lvdou.pojo.ContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Transactional
public class ContentCategoryServiceImpl     {

	@Autowired
	private ContentCategoryMapper contentCategoryMapper;

	public void save(ContentCategory contentCategory){
		try {
			contentCategoryMapper.insertSelective(contentCategory);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public void update(ContentCategory contentCategory){
		try {
			contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public void delete(Serializable id){
		try {
			contentCategoryMapper.deleteByPrimaryKey(id);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 批量删除 */
	public void deleteAll(Serializable[] ids){
		try {
			// 创建示范对象
			Example example = new Example(ContentCategory.class);
			// 创建条件对象
			Example.Criteria criteria = example.createCriteria();
			// 创建In条件
			criteria.andIn("id", Arrays.asList(ids));
			// 根据示范对象删除
			contentCategoryMapper.deleteByExample(example);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据主键id查询 */
	public ContentCategory findOne(Serializable id){
		try {
			return contentCategoryMapper.selectByPrimaryKey(id);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 查询全部 */
	public List<ContentCategory> findAll(){
		try {
			return contentCategoryMapper.selectAll();
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 多条件分页查询 */
	public PageResult findByPage(ContentCategory contentCategory, int page, int rows){
		try {
			PageInfo<ContentCategory> pageInfo = PageHelper.startPage(page, rows)
				.doSelectPageInfo(new ISelect() {
					@Override
					public void doSelect() {
						contentCategoryMapper.selectAll();
					}
				});
			return new PageResult(pageInfo.getTotal(), pageInfo.getList());
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

}
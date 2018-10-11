package com.lvdou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.content.service.ContentService;
import com.lvdou.mapper.ContentMapper;
import com.lvdou.pojo.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
/**
 * ContentServiceImpl 服务接口实现类
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2018-07-20 08:52:09
 * @version 1.0
 */
@Service(interfaceName = "com.lvdou.content.service.ContentService")
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentMapper contentMapper;
	@Autowired
	private RedisTemplate redisTemplate;

	/** 添加方法 */
	public void save(Content content){
		try {
			contentMapper.insertSelective(content);
			try{
				/** 清除Redis缓存 */
				redisTemplate.delete("content");
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 修改方法 */
	public void update(Content content){
		try {
			contentMapper.updateByPrimaryKeySelective(content);
			try{
				/** 清除Redis缓存 */
				redisTemplate.delete("content");
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据主键id删除 */
	public void delete(Serializable id){
		try {
			contentMapper.deleteByPrimaryKey(id);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 批量删除 */
	public void deleteAll(Serializable[] ids){
		try {
			// 创建示范对象
			Example example = new Example(Content.class);
			// 创建条件对象
			Example.Criteria criteria = example.createCriteria();
			// 创建In条件 delete from 表 where in (?,?,?)
			criteria.andIn("id", Arrays.asList(ids));
			// 根据示范对象删除
			contentMapper.deleteByExample(example);
			try{
				/** 清除Redis缓存 */
				redisTemplate.delete("content");
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据主键id查询 */
	public Content findOne(Serializable id){
		try {
			return contentMapper.selectByPrimaryKey(id);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 查询全部 */
	public List<Content> findAll(){
		try {
			return contentMapper.selectAll();
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 多条件分页查询 */
	public PageResult findByPage(Content content, int page, int rows){
		try {
			PageInfo<Content> pageInfo = PageHelper.startPage(page, rows)
				.doSelectPageInfo(new ISelect() {
					@Override
					public void doSelect() {
						contentMapper.selectAll();
					}
				});
			return new PageResult(pageInfo.getTotal(), pageInfo.getList());
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据广告分类id查询广告内容 */
	public List<Content> findContentByCategoryId(Long categoryId){

		/** ############# 从Redis中获取广告数据 ############## */
		List<Content> contentList = null;
		try{
			contentList = (List<Content>) redisTemplate.boundValueOps("content").get();
			if (contentList != null && contentList.size() > 0){
				System.out.println("========Redis中获取缓存数据============" + contentList);
				return contentList;
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}

		try {
			// 创建示范对象
			Example example = new Example(Content.class);
			// 创建条件对象
			Example.Criteria criteria = example.createCriteria();
			// 添加条件 category_id = ?
			criteria.andEqualTo("categoryId", categoryId);
			// 添加条件 status = 1
			criteria.andEqualTo("status", 1);

			// 排序 sort_order
			example.orderBy("sortOrder").asc();
			contentList = contentMapper.selectByExample(example);

			/** ############# 把广告数据存储到Redis中 ############## */
			try {
				// Spring-Data-Redis: 它会把contentList集合转化成字节进行存储
				redisTemplate.boundValueOps("content").set(contentList);
				System.out.println("======把广告数据存储到Redis中=========");
			}catch (Exception ex){
				ex.printStackTrace();
			}
			return contentList;
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

}
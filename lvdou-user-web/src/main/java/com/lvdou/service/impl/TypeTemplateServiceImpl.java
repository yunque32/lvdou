package com.lvdou.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.mapper.SpecificationOptionMapper;
import com.lvdou.mapper.TypeTemplateMapper;
import com.lvdou.pojo.SpecificationOption;
import com.lvdou.pojo.TypeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TypeTemplateServiceImpl  {
	@Resource
	private TypeTemplateMapper typeTemplateMapper;
	@Resource
	private SpecificationOptionMapper specificationOptionMapper;
	@Resource
	private RedisTemplate redisTemplate;

	/**
	 * 分页查询类型模版
	 * @param typeTemplate 模版实体
	 * @param page 当前页码
	 * @param rows 每页显示的记录数
	 * @return PageResult
	 */
	public PageResult findByPage(TypeTemplate typeTemplate,
								 Integer page, Integer rows){
		try{
			// 开始分页
			PageInfo<TypeTemplate> pageInfo = PageHelper.startPage(page, rows)
					.doSelectPageInfo(new ISelect() {
				@Override
				public void doSelect() {
					typeTemplateMapper.findAll(typeTemplate);
				}
			});
			return new PageResult(pageInfo.getTotal(), pageInfo.getList());
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 添加类型模版 */
	public void saveTypeTemplate(TypeTemplate typeTemplate){
		try{
			typeTemplateMapper.insertSelective(typeTemplate);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 修改类型模版 */
	public void updateTypeTemplate(TypeTemplate typeTemplate){
		try{
			typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 删除类型模版 */
	public void deleteTypeTemplate(Long[] ids){
		try{
			// 创建Example delete tb_type_template where id in (?,?,?)
			Example example = new Example(TypeTemplate.class);
			// 创建条件对象
			Example.Criteria criteria = example.createCriteria();
			// 添加in 条件 where in (?,?)
			criteria.andIn("id", Arrays.asList(ids));
			typeTemplateMapper.deleteByExample(example);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据主键id查询类型模版 */
	public TypeTemplate findOne(Long id){
		try {
			return typeTemplateMapper.selectByPrimaryKey(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** 根据类型模版id查询规格与规格选项 */
	public List<Map> findSpecByTemplateId(Long id){
		try {
			//  [{"id":27,"text":"网络", "options" : [{},{}]},
			//  {"id":32,"text":"机身内存","options":[{},{}]}]

			// 根据主键id查询类型模版
			TypeTemplate typeTemplate = findOne(id);
			// 获取规格json字符串，把它转化成 List<Map>
			// [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
			List<Map> specList = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
			// 迭代List集合
			for (Map map : specList){
				// 获取id的值 规格的主键id
				Long specId = Long.valueOf(map.get("id").toString());
				// 查询tb_specification_option规格选项
				// select * from tb_specification_option where spec_id = ?
				SpecificationOption so = new SpecificationOption();
				so.setSpecId(specId);
				List<SpecificationOption> soList = specificationOptionMapper.select(so);
				// map : {"id":27,"text":"网络"}
				// map: {"id":27,"text":"网络", "options" : [{},{}]}
				map.put("options", soList);
			}
			return specList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** 把类型模版中的品牌与规格选项数据存入Redis */
	public void saveToRedis(){
		try{
			// 查询全部的类型模版
			List<TypeTemplate> typeTemplateList = typeTemplateMapper.selectAll();
			// 迭代类型模版
			for (TypeTemplate typeTemplate : typeTemplateList){
				// 把类型模版中品牌添加到Redis
				// [{"id":1,"text":"联想"},{"id":3,"text":"三星"},{"id":2,"text":"华为"},
				// {"id":5,"text":"OPPO"},{"id":4,"text":"小米"},{"id":9,"text":"苹果"},{"id":8,"text":"魅族"},
				// {"id":6,"text":"360"},
				// {"id":10,"text":"VIVO"},{"id":11,"text":"诺基亚"},{"id":12,"text":"锤子"}]
				List<Map> brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);
				redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(), brandList);

				// 把类型模版中规格选项添加到Redis
				// [{"id":27,"text":"网络","options" : []},{"id":32,"text":"机身内存", "options" : []}]
				redisTemplate.boundHashOps("specList").put(typeTemplate.getId(),
						findSpecByTemplateId(typeTemplate.getId()));

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
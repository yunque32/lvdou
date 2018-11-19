package com.lvdou.service;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.mapper.SpecificationMapper;
import com.lvdou.mapper.SpecificationOptionMapper;
import com.lvdou.pojo.Specification;
import com.lvdou.pojo.SpecificationOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 规格服务接口实现层
 */
@Service
@Transactional(readOnly=false)
public class SpecificationServiceImpl  {

	@Autowired
	private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;
	

	public PageResult findByPage(Specification specification,
								 Integer page, Integer rows){
		try{
			// 开始分页
			PageInfo<Specification> pageInfo = PageHelper.startPage(page, rows)
					.doSelectPageInfo(new ISelect() {
				@Override
				public void doSelect() {
					specificationMapper.findAll(specification);
				}
			});
			return new PageResult(pageInfo.getTotal(), pageInfo.getList());
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}
	/** 添加规格与规格选项 */
	public void saveSpecification(Specification specification){
		try{
			// 往规格表插入数据
            specificationMapper.insertSelective(specification);
            System.out.println("主键id：" + specification.getId());

            // 往规格选项表循环插入数据
            specificationOptionMapper.save(specification);

		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据规格id查询规格选项 */
	public List<SpecificationOption> findOne(Long id){
		try{
		    return specificationOptionMapper.findSpecOptionBySpecId(id);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

    /** 修改规格与规格选项  */
    public void updateSpecification(Specification specification){
        try{
            // 修改往规格表数据
            specificationMapper.updateByPrimaryKeySelective(specification);

            /**########### 修改规格选项表数据 ###########*/

            // 第一步：删除规格选项表中的数据 spec_id
            // delete from tb_specification_option where spec_id = ?
            // 创建规格选项对象，封装删除条件 通用Mapper
            SpecificationOption so = new SpecificationOption();
            so.setSpecId(specification.getId());
            specificationOptionMapper.delete(so);

            // 第二步：往规格选项表插入数据
            specificationOptionMapper.save(specification);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 删除规格与规格选项 */
    public void deleteSpecification(Long[] ids){
        try{
            // 循环删除
            for (Long id : ids){
                // 根据规格id删除规格选项表的数据
                // delete from tb_specification_option where spec_id = ?
                // 创建规格选项对象，封装删除条件 通用Mapper
                SpecificationOption so = new SpecificationOption();
                so.setSpecId(id);
                specificationOptionMapper.delete(so);

                // 删除规格表中的数据
                specificationMapper.deleteByPrimaryKey(id);
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

	/** 查询规格列表(id,name) */
	public List<Map<String,Object>> findSpecByIdAndName(){
		try{
			return specificationMapper.findSpecByIdAndName();
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}
}
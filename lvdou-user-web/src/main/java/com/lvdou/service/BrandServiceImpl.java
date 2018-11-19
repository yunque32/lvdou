package com.lvdou.service;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.mapper.BrandMapper;
import com.lvdou.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl {

    @Autowired
    private BrandMapper brandMapper;

    public List<Brand> findAll() {
        // 开始分页
        // 第一个参数: 当前页码
        // 第二个参数：每页显示的记录数
        PageInfo<Brand> pageInfo = PageHelper.startPage(1, 25)
                .doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                brandMapper.selectAll();
            }
        });
        System.out.println("总记录数：" + pageInfo.getTotal());
        System.out.println("总页数：" + pageInfo.getPages());
        for (Brand brand : pageInfo.getList()){
            System.out.println(brand.getName());
        }
        return pageInfo.getList();
    }


    public PageResult findByPage(Brand brand, Integer page, Integer rows){
        try{
            // 开始分页
            PageInfo<Brand> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                   brandMapper.findAll(brand);
                }
            });
            System.out.println("pageInfo.getList: " + pageInfo.getList());
            // pageInfo.getList() --> PageInfo对象 --> Page
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 添加品牌 */
    public void saveBrand(Brand brand){
        try{
            // 通用Mapper中的保存数据方法
           brandMapper.insertSelective(brand);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 修改品牌 */
    public void updateBrand(Brand brand){
        try{
            // 通用Mapper中的修改数据方法
            brandMapper.updateByPrimaryKeySelective(brand);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 批量删除 */
    public void deleteBrand(Long[] ids){
        try{
            // delete from 表 where id in(?,?,?)
            brandMapper.deleteAll(ids);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 查询所有的品牌(id与name) */
    public List<Map<String,Object>> findBrandByIdAndName(){
        try{
           return brandMapper.findBrandByIdAndName();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

package com.lvdou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.mapper.FruitMapper;
import com.lvdou.pojo.Fruit;
import com.lvdou.sellergoods.service.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service(interfaceName = "com.lvdou.sellergoods.service.FruitService")
@Transactional
public class FruitServiceImpl implements FruitService {

    @Autowired
    private FruitMapper fruitMapper;

    @Override
    public List<Fruit> findAll() {
        // 第一个参数: 当前页码
        // 第二个参数：每页显示的记录数
        PageInfo<Fruit> pageInfo = PageHelper.startPage(1, 25)
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        fruitMapper.selectAll();
                    }
                });
        System.out.println("总记录数：" + pageInfo.getTotal());
        System.out.println("总页数：" + pageInfo.getPages());
        for (Fruit fruit : pageInfo.getList()){
            System.out.println(fruit.getFruitName());
        }
        return pageInfo.getList();
    }

    /**
     * 分页查询品牌
     * @param fruit 品牌对象
     * @param page 当前页码
     * @param rows 每页显示的记录数
     * @return 分页实体
     */
    public PageResult findByPage(Fruit fruit, Integer page, Integer rows){
        try{
            // 开始分页
            PageInfo<Fruit> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                        @Override
                        public void doSelect() {
                            fruitMapper.findAll(fruit);
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
    public void saveFruit(Fruit fruit){
        try{
            // 通用Mapper中的保存数据方法
            fruitMapper.insertSelective(fruit);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 修改品牌 */
    public void updateFruit(Fruit fruit){
        try{
            // 通用Mapper中的修改数据方法
            fruitMapper.updateByPrimaryKeySelective(fruit);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 批量删除 */
    public void deleteFruit(Long[] ids){
        try{
            // delete from 表 where id in(?,?,?)
            fruitMapper.deleteAll(ids);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 查询所有的品牌(id与name) */
    public List<Map<String,Object>> findFruitByIdAndName(){
        try{
            return fruitMapper.findFruitByIdAndName();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

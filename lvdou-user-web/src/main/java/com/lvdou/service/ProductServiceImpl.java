package com.lvdou.service;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.mapper.AgencyMapper;
import com.lvdou.mapper.ProductMapper;
import com.lvdou.mapper.ProducterMapper;
import com.lvdou.pojo.Agency;
import com.lvdou.pojo.Product;
import com.lvdou.pojo.Producter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductServiceImpl  {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private ProducterMapper producterMapper;

    public List<Product> findAll() {
        // 第一个参数: 当前页码
        // 第二个参数：每页显示的记录数
        PageInfo<Product> pageInfo = PageHelper.startPage(1, 25)
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        productMapper.selectAll();
                    }
                });

        return pageInfo.getList();
    }

    // 分页查询品牌
    public PageResult findByPage(Product product, Integer page, Integer rows){
        try{
            // 开始分页
            PageInfo<Product> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                        @Override
                        public void doSelect() {
                            productMapper.findAll(product);
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
    public void saveProduct(Product product){
        try{
            // 通用Mapper中的保存数据方法
            productMapper.insertSelective(product);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void updateProduct(Product product){
        try{
            // 通用Mapper中的修改数据方法
            productMapper.updateByPrimaryKeySelective(product);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 批量删除 */
    public void deleteProduct(Long[] ids){
        try{
            // delete from 表 where id in(?,?,?)
            productMapper.deleteAll(ids);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 查询所有的品牌(id与name) */
    public List<Map<String,Object>> findProductByIdAndName(){
        try{
            return productMapper.findProductByIdAndName();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public Map<String, Object> selectOne(Long productid) {
        return null;
        //productMapper.selectByid;
    }

    public Map<String, Object> selectTwo(Long productid) {
        Map<String, Object> map = new HashMap<>();
        Product product=productMapper.selectTwoIdByProductid(productid);

        Agency agency = agencyMapper.selectByPrimaryKey(product.getAgencyId());
        Producter producter = producterMapper.selectByPrimaryKey(1L);
        map.put("agency",agency);
        map.put("producter",producter);
        return map;
    }
}

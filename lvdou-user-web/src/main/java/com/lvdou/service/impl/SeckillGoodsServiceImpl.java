package com.lvdou.service.impl;

import com.lvdou.mapper.ProductMapper;
import com.lvdou.mapper.SeckillGoodsMapper;
import com.lvdou.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SeckillGoodsServiceImpl    {

    /** 注入数据访问接口代理对象 */
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private RedisTemplate redisTemplate;

    /** 查询秒杀商品列表 */
    public List<SeckillGoods> findSeckillGoods(){

        /** 秒杀商品列表 */
        List<SeckillGoods> seckillGoodsList = null;

        /** 从Redis缓存中获取秒杀商品列表 */
        try{
            seckillGoodsList =
                    redisTemplate.boundHashOps("seckillGoodsList").values();
            if (seckillGoodsList != null && seckillGoodsList.size() > 0){
                System.out.println("秒杀商品从Redis获取！");
                return seckillGoodsList;
            }
        }catch (Exception ex){
            System.out.println("从Redis获取描述商品出现异常！");
            ex.printStackTrace();
        }


        try{
            // 创建示范对象
            Example example = new Example(SeckillGoods.class);
            // 创建查询条件对象
            Example.Criteria criteria = example.createCriteria();
            // 审核状态: 1
            criteria.andEqualTo("status", 1);
            // 剩余库存数量大于0
            criteria.andGreaterThan("stockCount", 0);

            // 秒杀商品的开始时间小于等于当前系统时间 start_time <= ?
             criteria.andLessThanOrEqualTo("startTime", new Date());
//            // 秒杀商品的结束时间大于等于当前系统时间 end_time >= ?
             criteria.andGreaterThanOrEqualTo("endTime", new Date());

            seckillGoodsList = seckillGoodsMapper.selectByExample(example);
            /** 把秒杀商品存入Redis */
            try{
                for (SeckillGoods seckillGoods : seckillGoodsList){
                    redisTemplate.boundHashOps("seckillGoodsList")
                            .put(seckillGoods.getId(), seckillGoods);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return seckillGoodsList;

        }catch (Exception ex){
            throw  new RuntimeException(ex);
        }
    }

    /** 根据ID获取秒杀商品(从缓存中读取) */
    public SeckillGoods findOneFromRedis(Long id){

        //return  seckillGoodsMapper.selectByPrimaryKey(id);
         return (SeckillGoods)redisTemplate.boundHashOps("seckillGoodsList").get(id);
    }


}

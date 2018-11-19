package com.lvdou.service;

import com.lvdou.mapper.SeckillGoodsMapper;
import com.lvdou.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 秒杀任务调度服务类
 */
@Component
public class SeckillTaskService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /** 刷新秒杀商品定时任务方法 */
    @Scheduled(cron = "0 * * * * ?")
    public void refreshSeckillGoods(){
        System.out.println("执行了任务调度: " + new Date());

        /** 获取Redis中所有的秒杀商品的id */
        Set ids = redisTemplate.boundHashOps("seckillGoodsList").keys();

        /** 查询秒杀商品表，将符合条件的记录并且缓存中不存在的秒杀商品存入缓存 */
        Example example = new Example(SeckillGoods.class);
        // 创建查询条件对象
        Example.Criteria criteria = example.createCriteria();
        // 审核状态: 1
        criteria.andEqualTo("status", 1);
        // 剩余库存数量大于0
        criteria.andGreaterThan("stockCount", 0);

        // 秒杀商品的开始时间小于等于当前系统时间 start_time <= ?
        criteria.andLessThanOrEqualTo("startTime", new Date());
        // 秒杀商品的结束时间大于等于当前系统时间 end_time >= ?
        criteria.andGreaterThanOrEqualTo("endTime", new Date());
        // 排除Redis中已有的秒杀商品 not in (?,?,?)
        criteria.andNotIn("id", ids);
        // 查询数据
        List<SeckillGoods> seckillGoodsList = seckillGoodsMapper
                .selectByExample(example);
        // 循环存入缓存
        for (SeckillGoods seckillGoods : seckillGoodsList){
            redisTemplate.boundHashOps("seckillGoodsList")
                    .put(seckillGoods.getId(), seckillGoods);
        }
        System.out.println("将" + seckillGoodsList.size() + "条商品存入缓存！");
    }


    /** 过期秒杀商品的移除 */
    @Scheduled(cron = "* * * * * ?")
    public void removeSeckillGoods(){
        System.out.println("移除秒杀商品任务正在执行...");
        // 从Redis中获取所有的秒杀商品
        List seckillGoodsList = redisTemplate.boundHashOps("seckillGoodsList").values();
        // 循环秒杀商品列表
        for (Object obj : seckillGoodsList){
            SeckillGoods seckillGoods = (SeckillGoods) obj;
            // 判断秒杀商品是否过期
            if (seckillGoods.getEndTime().getTime() < new Date().getTime()){
                // 同步秒杀商品的剩余库存数量到数据库表
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                // 从Redis中删除该秒杀商品
                redisTemplate.boundHashOps("seckillGoodsList")
                        .delete(seckillGoods.getId());
                System.out.println("移除的秒杀商品：" + seckillGoods.getId());
            }
        }
        System.out.println("移除秒杀商品任务结束...");

    }

}

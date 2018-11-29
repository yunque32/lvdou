package com.lvdou.service.impl;

import com.lvdou.common.util.IdWorker;
import com.lvdou.mapper.SeckillGoodsMapper;
import com.lvdou.mapper.SeckillOrderMapper;
import com.lvdou.pojo.SeckillGoods;
import com.lvdou.pojo.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SeckillOrderServiceImpl     {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private IdWorker idWorker;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    /** 保存订单到Redis */
    public void submitOrderToRedis(Long id, String userId){
        try{
            // 从Redis中根据id获取秒杀商品
            SeckillGoods seckillGoods = (SeckillGoods)redisTemplate
                    .boundHashOps("seckillGoodsList").get(id);
            // 下单时扣减库存(Redis中缓存商品的库存)
            if (seckillGoods != null && seckillGoods.getStockCount() > 0){
                // 减库存
                seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
                // 判断剩余库存数量是否为零(秒光)
                if (seckillGoods.getStockCount() == 0){
                    // 0时修改数据库秒杀商品库存
                    seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                    // 删除Redis中秒杀商品
                    redisTemplate.boundHashOps("seckillGoodsList").delete(id);
                }else{ // 没有被秒光
                    // 重新设置该秒杀商品到Redis
                    redisTemplate.boundHashOps("seckillGoodsList").put(id, seckillGoods);
                }

                // 生成秒杀订单，存入Redis
                SeckillOrder seckillOrder = new SeckillOrder();
                // 设置订单id
                seckillOrder.setId(idWorker.nextId());
                // 设置秒杀商品id
                seckillOrder.setSeckillId(id);
                // 设置秒杀价格
                seckillOrder.setMoney(seckillGoods.getCostPrice());
                // 设置用户id
                seckillOrder.setUserId(userId);
                // 设置商家id
                seckillOrder.setSellerId(seckillGoods.getSellerId());
                // 设置创建时间
                seckillOrder.setCreateTime(new Date());
                // 设置状态码(未付款)
                seckillOrder.setStatus("0");

                // 存入Redis
                redisTemplate.boundHashOps("seckillOrderList").put(userId, seckillOrder);

            }

        }catch (Exception ex){
            throw  new RuntimeException(ex);
        }
    }

    /** 从Redis中获取用户的订单 */
    public SeckillOrder findOrderFromRedis(String userId){
        return (SeckillOrder)redisTemplate.boundHashOps("seckillOrderList").get(userId);
    }

    /** 保存订单到数据库 */
    public void saveOrder(String userId, String transactionId){
        try{
            /** 从Redis获取用户的订单 */
            SeckillOrder seckillOrder = findOrderFromRedis(userId);
            /** 判断秒杀订单 */
            if(seckillOrder != null) {
                // 设置支付时间
                seckillOrder.setPayTime(new Date());
                // 设置状态码(已付款)
                seckillOrder.setStatus("1");
                // 设置微信支付流水号
                seckillOrder.setTransactionId(transactionId);

                // 先把订单同步到数据库表
                seckillOrderMapper.insertSelective(seckillOrder);

                // 从Redis中删除该用户的订单
                redisTemplate.boundHashOps("seckillOrderList").delete(userId);
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 查询未支付订单(超时5分钟) */
    public List<SeckillOrder> findTimeoutOrderByRedis(){

        // 定义新的集合封装超时未支付订单
        List<SeckillOrder> seckillOrderList = new ArrayList<>();
        try{
            // 查询所有的未支付的订单
//            List seckillOrders = (List)redisTemplate.boundHashOps("seckillOrderList").values();
//            // 迭代，再查询超时5分钟的订单
//            for (Object obj : seckillOrders){
//                // 得到一个秒杀订单
//                SeckillOrder  seckillOrder = (SeckillOrder)obj;
//                // 用当前时间毫秒数-5分钟
//                long millsSeconds = new Date().getTime() - (5 * 60 * 1000);
//                // 判断订单是否超时
//                if (seckillOrder.getCreateTime().getTime() < millsSeconds){
//                    seckillOrderList.add(seckillOrder);
//                }
//            }
            return seckillOrderList;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 删除超时未支付订单 */
    public void deleteOrderFromRedis(SeckillOrder seckillOrder){
        try{
            // 删除Redis缓存中的订单
            redisTemplate.boundHashOps("seckillOrderList")
                    .delete(seckillOrder.getUserId());

            /** ######## 恢复库存数量 #######*/
            // 从Redis查询秒杀商品
            SeckillGoods seckillGoods = (SeckillGoods)redisTemplate
                    .boundHashOps("seckillGoodsList")
                    .get(seckillOrder.getSeckillId());
            if (seckillGoods != null){
                // 修改缓存中秒杀商品的库存 加1
                seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);
            }else{ // 秒光了
                // 从数据库表查询
                seckillGoods = seckillGoodsMapper.
                        selectByPrimaryKey(seckillOrder.getSeckillId());
                // 设置秒杀商品库存数量
                seckillGoods.setStockCount(1);
            }
            // 重新存入Redis
            redisTemplate.boundHashOps("seckillGoodsList")
                    .put(seckillGoods.getId(), seckillGoods);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

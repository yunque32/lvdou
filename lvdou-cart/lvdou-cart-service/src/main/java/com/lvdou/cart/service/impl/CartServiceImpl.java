package com.lvdou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvdou.cart.Cart;
import com.lvdou.cart.service.CartService;
import com.lvdou.mapper.ItemMapper;
import com.lvdou.pojo.Item;
import com.lvdou.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service(interfaceName = "com.lvdou.cart.service.impl.CartService")
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加SKU商品到购物车
     * @param carts 购物车集合 (一个Cart就代表一个商家购物车)
     * @param itemId SKU的id
     * @param num 购买数量
     * @return 修改后的购物车集合
     */
    public List<Cart> addItemToCart(List<Cart> carts, Long itemId, Integer num){
        try{

            /** 根据SKU商品ID查询SKU商品对象 */
            Item item = itemMapper.selectByPrimaryKey(itemId);
            /**  获取商家ID */
            String sellerId = item.getSellerId();
            /** 根据商家ID判断购物车集合中是否存在该商家的购物车*/
            Cart cart = searchCartBySellerId(carts, sellerId);
            // 判断该商家的购物车是否存在
            if (cart == null){ // 如果购物车集合中不存在该商家的购物车
                // 创建新的购物车对象
                cart = new Cart();
                // 设置商家的id
                cart.setSellerId(sellerId);
                // 设置商家的名称
                cart.setSellerName(item.getSeller());

                // 创建购物车集合
                List<OrderItem> orderItems = new ArrayList<>();
                // 创建购物车中的商品
                OrderItem orderItem = createOrderItem(item, num);
                // 添加购物车中的商品
                orderItems.add(orderItem);

                // 设置购物车集合
                cart.setOrderItems(orderItems);
                // 将新的购物车对象添加到购物车集合
                carts.add(cart);

            }else{ // 如果购物车集合中存在该商家的购物车
                // 判断购物车订单明细集合中是否存在该商品
                OrderItem orderItem = searchOrderItemByItemId(cart.getOrderItems(), itemId);
                // 如果没有，新增购物车订单明细
                if (orderItem == null){
                    // 创建新的OrderItem
                    orderItem = createOrderItem(item, num);
                    // 添加到物车订单明细集合中
                    cart.getOrderItems().add(orderItem);
                }else{
                    // 如果有，在原购物车订单明细上添加数量，更改金额
                    // 数量相加
                    orderItem.setNum(orderItem.getNum() + num);
                    // 金额小计
                    orderItem.setTotalFee(new BigDecimal(orderItem.
                            getPrice().doubleValue() * orderItem.getNum()));

                    // 由于购买数量可以为负数
                    if (orderItem.getNum() == 0){
                        // 从物车订单明细集合中删除该商品
                        cart.getOrderItems().remove(orderItem);
                    }
                    if (cart.getOrderItems().size() == 0){
                        // 从购物车集合中删除商家的购物车
                        carts.remove(cart);
                    }
                }
            }
            return carts;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 根据SKU的id从购物车订单明细集合中搜索一个OrderItem */
    private OrderItem searchOrderItemByItemId(List<OrderItem> orderItems, Long itemId) {
        for (OrderItem orderItem : orderItems){
            if (orderItem.getItemId().equals(itemId)){
                return orderItem;
            }
        }
        return null;
    }

    /** 创建购物车中的商品 */
    private OrderItem createOrderItem(Item item, Integer num) {
        OrderItem orderItem = new OrderItem();
        // 商品id
        orderItem.setItemId(item.getId());
        // SPU的id
        orderItem.setGoodsId(item.getGoodsId());
        // 商品标题
        orderItem.setTitle(item.getTitle());
        // 商品单价
        orderItem.setPrice(item.getPrice());
        // 商品购买数量
        orderItem.setNum(num);
        // 商品总金额(小计)
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
        // 商品图片地址
        orderItem.setPicPath(item.getImage());
        // 商家id
        orderItem.setSellerId(item.getSellerId());
        return orderItem;
    }

    /** 根据商家的id从购物车集合中搜索一个Cart */
    private Cart searchCartBySellerId(List<Cart> carts, String sellerId) {
        for (Cart cart : carts){
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }


    /**
     * 根据用户名获取购物车集合
     * @param username 登录用户名
     * @return 购物车集合
     */
    public List<Cart> findCartRedis(String username){
        List<Cart> carts = (List<Cart>)redisTemplate.boundValueOps("cart_" + username).get();
        if (carts == null){
            carts = new ArrayList<>();
        }
        return carts;
    }

    /**
     * 把购物车集合存储到Redis
     * @param username 登录用户名
     * @param carts 购物车集合
     */
    public void saveCartRedis(String username, List<Cart> carts){
        redisTemplate.boundValueOps("cart_" + username).set(carts);
    }

    /**
     * 购物车合并(把Cookie购物车数据 合并到 Redis中)
     * @param cookieCarts  Cookie中的购物车集合
     * @param redisCarts Redis中的购物车集合
     * @return 合并后的购物车集合
     */
    public List<Cart> mergeCart(List<Cart> cookieCarts, List<Cart> redisCarts){
        // 循环Cookie的购物车集合
        for (Cart cart : cookieCarts){
            // 循环购物车中的商品集合
            for (OrderItem orderItem : cart.getOrderItems()){
                // 不断的往redisCarts集合添加数据
                redisCarts = addItemToCart(redisCarts, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return redisCarts;
    }
}
package com.lvdou.cart.service;

import com.lvdou.cart.Cart;

import java.util.List;
/**
 * 购物车服务接口
 */
public interface CartService {
    /**
     * 添加SKU商品到购物车
     * @param carts 购物车集合 (一个Cart就代表一个商家购物车)
     * @param itemId SKU的id
     * @param num 购买数量
     * @return 修改后的购物车集合
     */
    List<Cart> addItemToCart(List<Cart> carts, Long itemId, Integer num);

    /**
     * 根据用户名获取购物车集合
     * @param username 登录用户名
     * @return 购物车集合
     */
    List<Cart> findCartRedis(String username);

    /**
     * 把购物车集合存储到Redis
     * @param username 登录用户名
     * @param carts 购物车集合
     */
    void saveCartRedis(String username, List<Cart> carts);

    /**
     * 购物车合并(把Cookie购物车数据 合并到 Redis中)
     * @param cookieCarts  Cookie中的购物车集合
     * @param redisCarts Redis中的购物车集合
     * @return 合并后的购物车集合
     */
    List<Cart> mergeCart(List<Cart> cookieCarts, List<Cart> redisCarts);
}

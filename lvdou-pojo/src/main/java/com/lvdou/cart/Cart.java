package com.lvdou.cart;

import com.lvdou.pojo.OrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车实体
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2018-08-02<p>
 */
public class Cart implements Serializable {
    /** 商家ID */
    private String sellerId;
    /** 商家名称 */
    private String sellerName;
    /** 购物车明细集合 */
    private List<OrderItem> orderItems;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
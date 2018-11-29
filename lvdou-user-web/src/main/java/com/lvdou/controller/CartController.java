package com.lvdou.controller;

import com.alibaba.fastjson.JSON;
import com.lvdou.pojo.Cart;
import com.lvdou.common.cookie.CookieUtils;
import com.lvdou.service.impl.CartServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    CartServiceImpl cartService ;
    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;

    /** 添加SKU商品到购物车 */
    @GetMapping("/addCart")
    // SpringMVC的跨域注解CORS
//    @CrossOrigin(origins = {"http://item.lvdou.com"},
//                allowCredentials = "true")
    public boolean addCart(Long itemId, Integer num){
        try{

            // 获取登录用户名
            String username = request.getRemoteUser();
            /** 获取购物车 */
            List<Cart> carts = findCart();
            //  调用服务层添加SKU商品到购物车(原来的购物车)，返加修改过的购物车

            carts = cartService.addItemToCart(carts, itemId, num);

            // 判断用户名
            if (StringUtils.isNoneBlank(username)) { // 已登录
                /** 已登录的用户，把购物车存储到Redis中 */
                cartService.saveCartRedis(username, carts);
            }else{ // 未登录
                /** 未登录的用户，把购物车存储到Cookie中 */
                CookieUtils.setCookie(request,
                        response,
                        CookieUtils.CookieName.LVDOE_CART,
                        JSON.toJSONString(carts),
                        3600 * 24, true);
            }

            /** 设置允许访问的域名（必须） */
            //response.setHeader("Access-Control-Allow-Origin","http://item.lvdou.com");
            /** 设置允许操作Cookie(不是必须) */
            //response.setHeader("Access-Control-Allow-Credentials","true");

            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 查看购物车 */
    @GetMapping("/findCart")
    public List<Cart> findCart(){

        // 获取登录用户名
        String username = request.getRemoteUser();

        // 定义购物车集合
        List<Cart> carts = null;

        // 判断用户名
        if (StringUtils.isNoneBlank(username)){ // 已登录
            /** 已登录的用户从Redis中获取购物车 List<Cart> */
            carts = cartService.findCartRedis(username);

            // 从Cookie中获取购物车集合json字符串
            String cartStr = CookieUtils.getCookieValue(request,
                    CookieUtils.CookieName.LVDOE_CART, true);

            // 判断Cookie中购物车是否为空
            if (StringUtils.isNoneBlank(cartStr)){
                // 把cartStr转化成List<Cart>
                List<Cart> cookieCarts = JSON.parseArray(cartStr, Cart.class);
                if (cookieCarts.size() > 0){
                    // 购物车合并(把Cookie中购物车数据 合并到 Redis)
                    carts = cartService.mergeCart(cookieCarts, carts);
                    // 把购物车存储到Redis中
                    cartService.saveCartRedis(username, carts);
                    // 删除Cookie中购物车数据(删除Cookie)
                    CookieUtils.deleteCookie(request, response,
                            CookieUtils.CookieName.LVDOE_CART);
                }
            }

        }else{ // 未登录
            /** 没有登录的用户从Cookie中获取购物车 List<Cart> json字符串: [{},{}]*/
            String cartStr = CookieUtils.getCookieValue(request,
                    CookieUtils.CookieName.LVDOE_CART, true);
            if (StringUtils.isBlank(cartStr)){
                cartStr = "[]";
            }
            // 把cartStr转化成List<Cart>
            carts = JSON.parseArray(cartStr, Cart.class);
        }
        return carts;
    }
}

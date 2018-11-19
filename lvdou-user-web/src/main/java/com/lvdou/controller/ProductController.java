package com.lvdou.controller;

import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.Product;
import com.lvdou.sellergoods.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    
    ProductService productService;

    @GetMapping("/findAll")
    public List<Product> findAll(){
        // Alt + Enter
        return productService.findAll();
    }

    /** 分页查询品牌 */
    @GetMapping("/findByPage")
    public PageResult findByPage(Product product,
                                 @RequestParam("page")Integer page,
                                 @RequestParam("rows")Integer rows) throws Exception{

        // GET请求中文乱码解决
        if (product != null && StringUtils.isNoneBlank(product.getProductName())){
            product.setProductName(new String(product.getProductName().getBytes("ISO8859-1"), "UTF-8"));
        }
        // response.data: List<Product> [{},{}] 与 总记录数
        // {"rows" : [{},{}], "total" : 100}
        return productService.findByPage(product, page, rows);
    }

    /** 添加品牌 */
    @PostMapping("/save")
    public boolean save(@RequestBody Product product){
        try{
            productService.saveProduct(product);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 修改品牌 */
    @PostMapping("/update")
    public boolean update(@RequestBody Product product){
        try{
            productService.updateProduct(product);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 批量删除 */
    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try{
            productService.deleteProduct(ids);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 查询所有的品牌 */
    @GetMapping("/findProductList")
    public List<Map<String, Object>> findProductList(){
        // [{id : 1, text : '华为'},{id : 2, text : '小米'}]
        return productService.findProductByIdAndName();
    }
    @GetMapping("/testCodeGen")
    public Map<String,String>testCodeGen(){
        Map<String, String> map = new HashMap<>();
        Integer a =1;
        Date b=new Date();
        map.put("productId",a.toString());
        map.put("pickingTime",b.toString());
        System.out.println(b.toString());
        return map;
    }
    @GetMapping("/queryyuan")
    public Map<String,Object>queryyuan(Long id){
        return productService.selectOne(id);
    };

    @GetMapping("/showAgencyAndProducter")
    public Map<String,Object>showAgencyAndProducter(@RequestParam("productid") Long productid){
        return productService.selectTwo(productid);
    }

}

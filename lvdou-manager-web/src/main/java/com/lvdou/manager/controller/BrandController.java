package com.lvdou.manager.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.Brand;
import com.lvdou.sellergoods.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    /** 引用服务 timeout:调用服务方法超时的毫秒数*/
    @Reference(timeout = 10000)
    private BrandService brandService;

    /** 处理Get请求 */
    @GetMapping("/findAll")
    public List<Brand> findAll(){
        System.out.println("brandService: " + brandService);
        // Alt + Enter
        return brandService.findAll();
    }

    /** 分页查询品牌 */
    @GetMapping("/findByPage")
    public PageResult findByPage(Brand brand,
                                 @RequestParam("page")Integer page,
                                 @RequestParam("rows")Integer rows) throws Exception{
        // GET请求中文乱码解决
        if (brand != null && StringUtils.isNoneBlank(brand.getName())){
            brand.setName(new String(brand.getName().getBytes("ISO8859-1"), "UTF-8"));
        }
        // response.data: List<Brand> [{},{}] 与 总记录数
        // {"rows" : [{},{}], "total" : 100}
        return brandService.findByPage(brand, page, rows);
    }

    /** 添加品牌 */
    @PostMapping("/save")
    public boolean save(@RequestBody Brand brand){
        try{
            brandService.saveBrand(brand);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 修改品牌 */
    @PostMapping("/update")
    public boolean update(@RequestBody Brand brand){
        try{
            brandService.updateBrand(brand);
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
            brandService.deleteBrand(ids);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 查询所有的品牌 */
    @GetMapping("/findBrandList")
    public List<Map<String, Object>> findBrandList(){
        // [{id : 1, text : '华为'},{id : 2, text : '小米'}]
        return brandService.findBrandByIdAndName();
    }
}

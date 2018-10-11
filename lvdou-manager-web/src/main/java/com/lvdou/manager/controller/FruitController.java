package com.lvdou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.Fruit;
import com.lvdou.sellergoods.service.FruitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fruit")
public class FruitController {
    
    @Reference(timeout = 10000)
    private FruitService fruitService;

    @GetMapping("/findAll")
    public List<Fruit> findAll(){
        System.out.println("fruitService: " + fruitService);
        // Alt + Enter
        return fruitService.findAll();
    }

    /** 分页查询品牌 */
    @GetMapping("/findByPage")
    public PageResult findByPage(Fruit fruit,
                                 @RequestParam("page")Integer page,
                                 @RequestParam("rows")Integer rows) throws Exception{
        System.out.println("yyyyyyyyyyyyyyyyyyy");
        // GET请求中文乱码解决
        if (fruit != null && StringUtils.isNoneBlank(fruit.getFruitName())){
            fruit.setFruitName(new String(fruit.getFruitName().getBytes("ISO8859-1"), "UTF-8"));
        }
        // response.data: List<Fruit> [{},{}] 与 总记录数
        // {"rows" : [{},{}], "total" : 100}
        return fruitService.findByPage(fruit, page, rows);
    }

    /** 添加品牌 */
    @PostMapping("/save")
    public boolean save(@RequestBody Fruit fruit){
        try{
            fruitService.saveFruit(fruit);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 修改品牌 */
    @PostMapping("/update")
    public boolean update(@RequestBody Fruit fruit){
        try{
            fruitService.updateFruit(fruit);
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
            fruitService.deleteFruit(ids);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /** 查询所有的品牌 */
    @GetMapping("/findFruitList")
    public List<Map<String, Object>> findFruitList(){
        // [{id : 1, text : '华为'},{id : 2, text : '小米'}]
        return fruitService.findFruitByIdAndName();
    }
    
}

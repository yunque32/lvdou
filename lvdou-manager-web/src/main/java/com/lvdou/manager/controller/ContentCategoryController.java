package com.lvdou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.content.service.ContentCategoryService;
import com.lvdou.pojo.ContentCategory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

	@Reference(timeout = 10000)
	private ContentCategoryService contentCategoryService;

	/** 多条件分页查询方法 */
	@GetMapping("/findByPage")
	public PageResult save(ContentCategory contentCategory,
						   @RequestParam(value="page", defaultValue="1")Integer page,
						   @RequestParam(value="rows", defaultValue="10")Integer rows) {
		try {
			return contentCategoryService.findByPage(contentCategory, page, rows);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	/** 根据主键id查询方法 */
	@GetMapping("/findOne")
	public ContentCategory findOne(Long id) {
		try {
			return contentCategoryService.findOne(id);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	/** 添加方法 */
	@PostMapping("/save")
	public boolean save(@RequestBody ContentCategory contentCategory) {
		try {
			contentCategoryService.save(contentCategory);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/** 修改方法 */
	@PostMapping("/update")
	public boolean update(@RequestBody ContentCategory contentCategory) {
		try {
			contentCategoryService.update(contentCategory);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/** 删除方法 */
	@GetMapping("/delete")
	public boolean delete(Long[] ids) {
		try {
			contentCategoryService.deleteAll(ids);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/** 查询所有的内容分类 */
	@GetMapping("/findAll")
	public List<ContentCategory> findAll(){
		try {
			return contentCategoryService.findAll();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

}

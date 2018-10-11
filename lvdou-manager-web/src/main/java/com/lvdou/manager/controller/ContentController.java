package com.lvdou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.content.service.ContentService;
import com.lvdou.pojo.Content;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference(timeout = 10000)
	private ContentService contentService;

	/** 多条件分页查询方法 */
	@GetMapping("/findByPage")
	public PageResult save(Content content,
						   @RequestParam(value="page", defaultValue="1")Integer page,
						   @RequestParam(value="rows", defaultValue="10")Integer rows) {
		try {
			return contentService.findByPage(content, page, rows);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	/** 根据主键id查询方法 */
	@GetMapping("/findOne")
	public Content findOne(Long id) {
		try {
			return contentService.findOne(id);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	/** 添加方法 */
	@PostMapping("/save")
	public boolean save(@RequestBody Content content) {
		try {
			contentService.save(content);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/** 修改方法 */
	@PostMapping("/update")
	public boolean update(@RequestBody Content content) {
		try {
			contentService.update(content);
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
			contentService.deleteAll(ids);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

}

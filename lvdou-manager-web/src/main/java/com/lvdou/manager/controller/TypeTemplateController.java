package com.lvdou.manager.controller;

import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.TypeTemplate;
import com.lvdou.service.impl.TypeTemplateServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * TypeTemplateController
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	private TypeTemplateServiceImpl typeTemplateService;

	/** 分页查询规格 */
	@GetMapping("/findByPage")
	public PageResult findByPage(TypeTemplate typeTemplate,
								 Integer page, Integer rows){
		try {
			/** GET请求参数转码 */
			if (typeTemplate != null && StringUtils.
					isNoneBlank(typeTemplate.getName())) {
				typeTemplate.setName(new String(typeTemplate
						.getName().getBytes("ISO8859-1"), "UTF-8"));
			}

			return typeTemplateService.findByPage(typeTemplate, page, rows);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	/** 添加类型模版 */
	@PostMapping("/save")
	public boolean saveTypeTemplate(@RequestBody TypeTemplate typeTemplate){
		try{
			typeTemplateService.saveTypeTemplate(typeTemplate);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}


	/** 修改类型模 */
	@PostMapping("/update")
	public boolean updateTypeTemplate(@RequestBody TypeTemplate typeTemplate){
		try{
			typeTemplateService.updateTypeTemplate(typeTemplate);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/** 删除类型模板 */
	@GetMapping("/delete")
	public boolean deleteTypeTemplate(Long[] ids){
		try{
			typeTemplateService.deleteTypeTemplate(ids);
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/** 把类型模版中的品牌与规格选项数据存入Redis */
	@GetMapping("/updateRedis")
	public boolean updateRedis(){
		try{
			typeTemplateService.saveToRedis();
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
}

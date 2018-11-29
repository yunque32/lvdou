package com.lvdou.service.impl;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.mapper.SellerMapper;
import com.lvdou.pojo.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Transactional(readOnly=false)
public class SellerServiceImpl  {
	
	/** 注入数据访问层代理对象 */
	@Resource
	private SellerMapper sellerMapper;

	/** 添加商家 */
	public void saveSeller(Seller seller){
		try {
			// 设置商家审核状态(未审核)
			seller.setStatus("0");
			seller.setCreateTime(new Date());
			sellerMapper.insertSelective(seller);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public PageResult findByPage(Seller seller, Integer page, Integer rows){
		try {
			// 开启分页查询
			PageInfo<Seller> pageInfo = PageHelper.startPage(page, rows)
					.doSelectPageInfo(new ISelect() {
				@Override
				public void doSelect() {
					sellerMapper.findAll(seller);
				}
			});
			return new PageResult(pageInfo.getTotal(), pageInfo.getList());
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 修改商家状态
	 */
	public void updateStatus(String sellerId, String status){
		try {
			// 创建商家对象 update tb_seller set status = ? where seller_id = ?
            Seller seller = new Seller();
            seller.setSellerId(sellerId);
            seller.setStatus(status);
            sellerMapper.updateByPrimaryKeySelective(seller);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据sellerId查询商家对象 */
	public Seller findOne(String username){
		try {
			return sellerMapper.selectByPrimaryKey(username);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}
}
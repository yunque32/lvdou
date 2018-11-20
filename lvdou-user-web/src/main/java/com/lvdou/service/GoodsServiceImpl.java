package com.lvdou.service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.mapper.*;
import com.lvdou.pojo.Goods;
import com.lvdou.pojo.GoodsDesc;
import com.lvdou.pojo.Item;
import com.lvdou.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


@Service
@Transactional
public class GoodsServiceImpl   {

	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsDescMapper goodsDescMapper;
	@Autowired
	private ItemCatMapper itemCatMapper;
	@Autowired
	private BrandMapper brandMapper;
	@Autowired
	private SellerMapper sellerMapper;
	@Autowired
	private ItemMapper itemMapper;

	public List<Goods> findAllGoods() {
		return goodsMapper.selectAll();
	}

	/** 保存商品 */
	public void saveGoods(Goods goods){
		/*try {

			goods.setAuditStatus("0");
			goodsMapper.insertSelective(goods);

			//** 往tb_goods_desc表插入数据 (商品描述表)
			goods.getGoodsDesc().setGoodsId(goods.getId());
			goodsDescMapper.insertSelective(goods.getGoodsDesc());
			// 添加sku商品信息
			saveItems(goods);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}*/
	}

	/** 添加sku商品信息 */
	private void saveItems(Goods goods){
		// 判断是否启用规格
		/*if ("1".equals(goods.getIsEnableSpec())) {
			// 启用规格

			// 往tb_item表插入数据 (SKU表) goods.items
			for (Item item : goods.getItems()) {
				// item: {spec:{}, price:0, num:9999, status:'0', isDefault:'0'}

				// 设置SKU商品的标题 : Apple iPhone 8 Plus (A1864) 联通4G 64G
				StringBuilder title = new StringBuilder(goods.getTitle());
				// spec: {"网络":"联通4G","机身内存":"64G"}
				// 把spec的json字符串转化成map集合
				Map specMap = JSON.parseObject(item.getSpec());
				// 循环map集合的value
				for (Object spec : specMap.values()) {
					title.append(" " + spec.toString());
				}
				item.setTitle(title.toString());

				// 设置SKU商品的其它信息
				setItemInfo(goods, item);

				// 往tb_item表插入数据（SKU表）
				itemMapper.insertSelective(item);
			}
		}else{ // 没有启用规格

			// 创建Item
			Item item = new Item();
			// {spec:{}, price:0, num:9999, status:'0', isDefault:'0'}
			// 设置SKU商品价格
			item.setPrice(goods.getPrice());
			// 设置SKU商品库存数据
			item.setNum(100);
			// 设置SKU商品的状态码
			item.setStatus("1");
			// 设置SKU商品是否为默认
			item.setIsDefault("1");
			// 设置SKU商品的规格
			item.setSpec("{}");
			// 设置SKU商品标题
			item.setTitle(goods.getTitle());

			// 设置SKU商品的其它信息
			setItemInfo(goods, item);

			// 往tb_item表插入数据（SKU表）
			itemMapper.insertSelective(item);
		}*/
	}

	/** 设置SKU商品的其它信息 */
	private void setItemInfo(Goods goods, Item item){

		// 从商品描述表取一张图片
		/*String itemImages = goods.getGoodsDesc().getItemImages();
		if (StringUtils.isNotEmpty(itemImages)) {
			// [{"color":"金色","url":"http://image.lvdou.com/jd/wKgMg1qtKEOATL9nAAFti6upbx4132.jpg"}]
			List<Map> imagesList = JSON.parseArray(itemImages, Map.class);
			// 设置SKU商品的图片
			item.setImage(imagesList.get(0).get("url").toString());
		}
		// 设置SKU商品三级分类id
		item.setCategoryid(goods.getCategory3Id());
		// 设置SKU商品的创建时间
		item.setCreateTime(new Date());
		//  设置SKU商品的修改时间
		item.setUpdateTime(item.getCreateTime());
		// 设置SKU关联的SPU商品的id
		item.setGoodsId(goods.getId());
		// 设置SKU关联的商家id
		item.setSellerId(goods.getSellerId());
		// 设置SKU的商品分类名称
		item.setCategory(itemCatMapper.
				selectByPrimaryKey(goods.getCategory3Id()).getName());

		// 设置SKU的商品品牌名称
		item.setBrand(brandMapper.
				selectByPrimaryKey(goods.getBrandId()).getName());

		// 设置SKU的商品商家店铺名称
		item.setSeller(sellerMapper.selectByPrimaryKey(goods.getSellerId()).getNickName());*/
	}


	/** 修改商品(SPU、商品描述、SKU) */
	public void updateGoods(Goods goods){
		/*try{
			// 设置未申请状态
			goods.setAuditStatus("0");
			// 修改SPU商品表
			goodsMapper.updateByPrimaryKeySelective(goods);

			// 修改商品描述表
			goodsDescMapper.updateByPrimaryKeySelective(goods.getGoodsDesc());

			// 修改SKU表(tb_item)
			// 根据goods_id删除SKU数据 delete from tb_item where goods_id = ?
			Item item = new Item();
			item.setGoodsId(goods.getId());
			itemMapper.delete(item);

			// 往tb_item表添加新的数据
			// 添加sku商品信息
			saveItems(goods);

		}catch (Exception ex){
			throw new RuntimeException(ex);
		}*/
	}


	/** 多条件分页查询商品 */
	public PageResult findGoodsByPage(Goods goods, Integer page, Integer rows){
		try{
			// 开启分页
			PageInfo<Map<String,Object>> pageInfo = PageHelper.startPage(page, rows)
					.doSelectPageInfo(new ISelect() {
				@Override
				public void doSelect() {
					goodsMapper.findAllByWhere(goods);
				}
			});
			// 迭代商品(添加分类名称)
			for (Map<String,Object> map : pageInfo.getList()){
				// 查询一级分类
				ItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(map.get("category1Id"));
				// 添加一级分类名称
				map.put("category1Name", itemCat1 != null ? itemCat1.getName() : "");

				// 查询二级分类
				ItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(map.get("category2Id"));
				// 添加二级分类名称
				map.put("category2Name", itemCat2 != null ? itemCat2.getName() : "");

				// 查询三级分类
				ItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(map.get("category3Id"));
				// 添加三级分类名称
				map.put("category3Name", itemCat3 != null ? itemCat3.getName() : "");
			}

			return new PageResult(pageInfo.getTotal(), pageInfo.getList());
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据主键id查询商品 */
	public Goods findOne(Long id){
		try{
			// 查询tb_goods表中的数据(SPU)
			Goods goods = goodsMapper.selectByPrimaryKey(id);

			// 查询商品描述表中的数据
			GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
			goods.setGoodsDesc(goodsDesc);

			// 查询SKU表中的数据(根据SPU的goods_id)
			Item item = new Item();
			item.setGoodsId(id);
			List<Item> itemList = itemMapper.select(item);
			goods.setItems(itemList);

			return goods;

		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 修改商品状态码 */
	public void updateAuditStatus(Long[] ids, String status){
		try{
			goodsMapper.updateAuditStatus(ids, status);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 修改商品删除状态 */
	public void updateDeleteStatus(Long[] ids){
		try{
			goodsMapper.updateDeleteStatus(ids, "1");
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据SPU的id查询SKU数据 */
	public List<Item> findItemByGoodsIdAndStatus(Long[] ids, String status){
		try{
			// SELECT * FROM tb_item  WHERE goods_id IN(149187842867974,149187842867978) AND STATUS = 1
			Example example = new Example(Item.class);
			// 创建查询条件
			Example.Criteria criteria = example.createCriteria();
			// 添加in条件
			criteria.andIn("goodsId", Arrays.asList(ids));
			// 添加 =
			criteria.andEqualTo("status", status);
			return itemMapper.selectByExample(example);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/** 根据goodsId查询商品信息 */
	public Map<String,Object> getItem(Long goodsId){
		/*try{
			Map<String,Object> dataModel = new HashMap<>();
			// 查询SPU表中的数据
			Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);

			// 查询商品描述表中的数据
			GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);


			// 查询商品的分类
			if (goods.getCategory3Id() != null && goods.getCategory3Id() > 0) {
				// 查询一级商品分类
				ItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id());
				dataModel.put("itemCat1", itemCat1 != null ? itemCat1.getName() : "");

				// 查询二级商品分类
				ItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id());
				dataModel.put("itemCat2", itemCat2 != null ? itemCat2.getName() : "");

				// 查询三级商品分类
				ItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
				dataModel.put("itemCat3", itemCat3 != null ? itemCat3.getName() : "");
			}

			// 查询SKU表中的数据
			// 创建示范对象
			Example example = new Example(Item.class);
			// 创建条件对象
			Example.Criteria criteria = example.createCriteria();
			// 添加条件 goods_id = ?
			criteria.andEqualTo("goodsId", goodsId);
			// 状态码
			criteria.andEqualTo("status", "1");
			// 排序 (把默认的SKU商品排在最前面)
			example.orderBy("isDefault").desc();
			// 查询SKU数据 [{},{}]
			List<Item> itemList = itemMapper.selectByExample(example);

			// 把itemList转化成json字符串
			dataModel.put("itemList", JSON.toJSONString(itemList));

			return dataModel;
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}*/
		return null;
	};


}
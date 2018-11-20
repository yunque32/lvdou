package com.lvdou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.common.pojo.PageResult;
import com.lvdou.pojo.Goods;
import com.lvdou..service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.*;
import java.util.Arrays;

@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference(timeout = 10000)
	private GoodsService goodsService;
//	@Autowired
//	private JmsTemplate jmsTemplate;
//	@Autowired
//	private Destination solrQueue;
//	@Autowired
//	private Destination solrDeleteQueue;
//	@Autowired
//	private Destination pageTopic;
//	@Autowired
//	private Destination pageDeleteTopic;

	/** 多条件分页查询商品 */
	@GetMapping("/findByPage")
	public PageResult findByPage(Goods goods, Integer page, Integer rows){
		try{
			// Get请求中文转码
			if (goods != null && StringUtils.isNoneBlank(goods.getGoodsName())){
				goods.setGoodsName(new String(goods.getGoodsName()
						.getBytes("ISO8859-1"), "UTF-8"));
			}
			// 审核状态(未审核)
			//goods.setAuditStatus("0");

			// 调用服务层查询数据
			return goodsService.findGoodsByPage(goods, page, rows);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	/** 审核商品(修改商品状态码) */
	/*@GetMapping("/updateStatus")
	public boolean updateStatus(Long[] ids, String status){
		try{
			goodsService.updateAuditStatus(ids, status);
			// 发送消息到 --> 消息中间件(jms)
			// 吞 500
			// 吐 100
			// 异步处理
			*//** 判断状态 *//*
			if ("1".equals(status)){ // 审核
				// 发送消息到消息中间件(创建索引)
				jmsTemplate.send(solrQueue, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						MapMessage mapMessage = session.createMapMessage();
						mapMessage.setObject("ids", Arrays.asList(ids));
						mapMessage.setString("status", status);
						return mapMessage;
					}
				});
				// 发送消息到消息中间件(生成商品静态的html页面)
				for (Long id : ids) {
					jmsTemplate.send(pageTopic, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(id.toString());
						}
					});
				}
			}
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}*/

	/** 删除商家商品(修改商品删除状态) */
	/*@GetMapping("/delete")
	public boolean delete(Long[] ids){
		try{
			goodsService.updateDeleteStatus(ids);

			// 发送消息到消息中间件(删除索引)
			jmsTemplate.send(solrDeleteQueue, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createObjectMessage(ids);
				}
			});
			// 发送消息到消息中间件(删除静态页面)
            jmsTemplate.send(pageDeleteTopic, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(ids);
                }
            });
			return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
*/


}

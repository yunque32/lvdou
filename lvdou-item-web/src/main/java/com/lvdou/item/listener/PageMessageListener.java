package com.lvdou.item.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.sellergoods.service.GoodsService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * 生成静态页面的消息监听器类
 */
public class PageMessageListener implements SessionAwareMessageListener<TextMessage> {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Reference(timeout = 10000)
    private GoodsService goodsService;
    @Value("${page.dir}")
    private String pageDir;

    @Override
    public void onMessage(TextMessage textMessage, Session session) throws JMSException {
        try {
            System.out.println("===PageMessageListener===");
            // 接收消息的内容
            String goodsId = textMessage.getText();
            System.out.println("goodsId: " + goodsId);

            /** ######## 生成商品的静态页面 ####### */
            // 获取模版对象
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("item.ftl");
            // 获取数据模型
            Map<String,Object> dataModel = goodsService.getItem(Long.valueOf(goodsId));
            // 创建输出流
            OutputStreamWriter writer = new OutputStreamWriter(new
                    FileOutputStream(pageDir + goodsId + ".html"), "UTF-8");
            // 填充模版生成静态的html页面
            template.process(dataModel, writer);

            writer.flush();
            writer.close();

        }catch (Exception ex){
            throw  new RuntimeException(ex);
        }

    }
}

package com.lvdou.search.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lvdou.search.service.ItemSearchService;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.Arrays;

/**
 * 删除索引的消息监听器类
 */
public class DeleteMessageListener implements SessionAwareMessageListener<ObjectMessage>{

    @Reference(timeout = 30000)
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {
        System.out.println("======DeleteMessageListener========");
        // 接收消息内容
        Long[] ids = (Long[])objectMessage.getObject();
        System.out.println("ids: " + Arrays.toString(ids));
        itemSearchService.delete(Arrays.asList(ids));
    }
}

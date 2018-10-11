package com.lvdou.item.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.File;
import java.util.Arrays;

/**
 * 删除商品静态页面消息监听器类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2018-07-28<p>
 */
public class DeleteMessageListener implements SessionAwareMessageListener<ObjectMessage> {

    @Value("${page.dir}")
    private String pageDir;

    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {
        System.out.println("======DeleteMessageListener==========");
        // 获取消息的内容
        Long[] goodsIds = (Long[]) objectMessage.getObject();
        System.out.println("goodsIds: " + Arrays.toString(goodsIds));

        /** ############ 删除商品的静态页面 ############# */
        for (Long goodsId : goodsIds){
            File file = new File(pageDir + goodsId + ".html");
            if (file.exists()){
                // 删除文件
                file.delete();
            }
        }
    }
}

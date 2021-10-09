/**
 * @(#)SyncProducer.java, 10月 04, 2021.
 * <p>
 * Copyright 2021 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package basicMsg;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import java.io.UnsupportedEncodingException;

/**
 * @author junex
 */
@Slf4j
public class SyncProducer {
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        //设置生产者组
        defaultMQProducer.setProducerGroup("syncProducer");

        //设置nameserver
        defaultMQProducer.setNamesrvAddr("localhost:9876");

        //启动生产者
        defaultMQProducer.start();

        for (int i = 0; i < 10; i++) {
            //构建消息 topic tag 内容
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //同步发送，且返回结果
            SendResult sendResult = defaultMQProducer.send(msg);
            System.out.println("发送结果"+sendResult);
        }
        //关闭生产者
        defaultMQProducer.shutdown();
    }

}
/**
 * @(#)DelayProducer.java, 10月 06, 2021.
 * <p>
 * Copyright 2021 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package delayMsg;

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
public class DelayProducer {

    public static void main(String[] args)
            throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException,
            InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("delay_producer_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        Message msg = new Message("delayTopic", "TagA", "delay message".getBytes(RemotingHelper.DEFAULT_CHARSET));

        msg.setDelayTimeLevel(9);

        SendResult sendResult = producer.send(msg);

        System.out.println("send result " + sendResult);

        producer.shutdown();
    }

}
/**
 * @(#)OrderedProducer.java, 10月 06, 2021.
 * <p>
 * Copyright 2021 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package orderedMsg;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author junex
 */
public class OrderedProducer {

    public static void main(String[] args)
            throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException,
            InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("order_producer_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        List<OrderEntity> list = buildOrderList();

        for (int i = 0; i < list.size(); ++i) {
            int orderId = list.get(i).getOrderId();
            Message msg = new Message("orderTopic", "TagA", "KEY" + i,
                    list.get(i).toString().getBytes(RemotingHelper.DEFAULT_CHARSET));

            SendResult result = producer.send(msg, (mqs, message, o) -> {
                Integer id = (Integer) o;
                int index = id % mqs.size();
                return mqs.get(index);
            }, orderId);

            System.out.println(orderId + "send result " + result);
        }
        producer.shutdown();
    }

    private static List<OrderEntity> buildOrderList() {
        List<OrderEntity> res =  new ArrayList<>();

        OrderEntity order1 = new OrderEntity(147,"加入购物车");
        OrderEntity order2 = new OrderEntity(147,"下单");
        OrderEntity order3 = new OrderEntity(147,"付款");
        OrderEntity order4 = new OrderEntity(147,"完成");

        OrderEntity order5 = new OrderEntity(258,"加入购物车");
        OrderEntity order6 = new OrderEntity(258,"下单");

        OrderEntity order7 = new OrderEntity(369,"加入购物车");
        OrderEntity order8 = new OrderEntity(369,"下单");
        OrderEntity order9 = new OrderEntity(369,"付款");

        res.add(order1);
        res.add(order2);
        res.add(order3);
        res.add(order4);
        res.add(order5);
        res.add(order6);
        res.add(order7);
        res.add(order8);
        res.add(order9);

        return res;
    }
}

@Data
@AllArgsConstructor
class OrderEntity {
    int orderId;
    String desc;

}


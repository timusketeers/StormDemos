package com.howbuy.onlinecalc.utils;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * ��kafka��Ϣ���ķ�����Ϣ..
 * @author li.zhang
 *
 */
public class KafkaMsgSender
{
    /**
     * ��kafka������Ϣ..
     * @param topic ����
     * @param message ��Ϣ����
     */
    public static void dispatch(String topic, String message)
    {
        Properties props = new Properties();
        
        //ע������Ҫ��hostname����Ҫֱ��дip��ַ.
        props.put("metadata.broker.list","kafka:9092");
        props.put("zookeeper.connect", "zookeeper:12181,zookeeper:22181,zookeeper:32181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("producer.type", "sync");
        props.put("compression.codec", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, message); //topic, message
        producer.send(data);
        
        producer.close();
        
    }
}

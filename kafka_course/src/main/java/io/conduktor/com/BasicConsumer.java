package io.conduktor.com;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class BasicConsumer {

    private static final Logger log = LoggerFactory.getLogger(BasicConsumer.class.getName());

    public static void main(String[] args) {
        String topic = "first_topic";
        String group_id = "new_group";
        log.info("setting up properties");
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,group_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        log.info("creating kafka consumer");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(topic));

        while(true){
        ConsumerRecords<String,String> consumerrecords =
                consumer.poll(Duration.ofMillis(100));

        for(ConsumerRecord<String,String> record  : consumerrecords){
            log.info("records \n" +
                    "Topic :-"+record.topic()+ "\n" +
                    "Key :-"+record.key()+"\n" +
                    "Value :-"+record.value() + "\n"+
                    "Offset :-"+record.offset() + "\n"+
                    "Partition :-"+record.partition()+"\n"+
                    "Timestamp :-"+record.timestamp()) ;

        }
    }}

}

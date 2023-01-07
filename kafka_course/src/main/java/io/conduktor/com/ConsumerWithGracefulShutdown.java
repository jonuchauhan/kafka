package io.conduktor.com;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerWithGracefulShutdown {

    private static final Logger log = LoggerFactory.getLogger(ConsumerWithGracefulShutdown.class.getName());

    public static void main(String[] args) {
        String topic="fifth_topic";
        String group = "third_group";
        log.info("setting up properties");
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,group);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(properties);

        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                log.info("shutdown detected");
                consumer.wakeup();
                try{
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        consumer.subscribe(Arrays.asList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> consumerrecords = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : consumerrecords) {
                    log.info("records \n" +
                            "Topic :-"+record.topic()+ "\n" +
                            "Key :-"+record.key()+"\n" +
                            "Value :-"+record.value() + "\n"+
                            "Offset :-"+record.offset() + "\n"+
                            "Partition :-"+record.partition()+"\n"+
                            "Timestamp :-"+record.timestamp()) ;
                }

            }
        }
        catch (WakeupException e){
            log.info("wakeup exception detected");
        }
        catch (Exception e){
            log.info("Unexpected exception occured");
        }finally {
            log.info("closing consumer");
            consumer.close();
        }



    }
}

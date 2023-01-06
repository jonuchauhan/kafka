package io.conduktor.com;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithKeys {
    private static final Logger log = LoggerFactory.getLogger(ProducerWithKeys.class.getName());
    private static String topic="second_topic";
    public static void main(String[] args) {
        log.info("setting up properties");

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        for(int i=0; i<=20;i++){
            ProducerRecord<String,String> producerrecord=new ProducerRecord<>(topic,"id_"+i,"hello kafka_"+i);
            producer.send(producerrecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    log.info("Metadata  \n"+
                            "Topic :-" + metadata.topic() +"\n"+
                            "Partitions :-"+ metadata.partition() +"\n"+
                            "Offset :-" + metadata.offset()+"\n"+
                            "Timestamp :-"+metadata.timestamp()
                            );
                }
            });

        }

        producer.close();


    }
}

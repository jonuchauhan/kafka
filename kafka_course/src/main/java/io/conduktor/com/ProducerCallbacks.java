package io.conduktor.com;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
public class ProducerCallbacks {

    private static final Logger logger = LoggerFactory.getLogger(ProducerCallbacks.class.getName());
    public static String topic ="second_topic";
    public static void main(String[] args) {
        logger.info("setting up properties");
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());


        KafkaProducer<String , String> producer = new KafkaProducer<>(properties);

        ProducerRecord<String , String> producerRecord = new ProducerRecord<>(topic,"hello kafka with callback");

        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if(e==null){
                    logger.info("Metadata \n" +
                            "Topic: " + metadata.topic()+"\n"+
                            "Partition: " + metadata.partition()+ "\n"+
                            "Offset: " + metadata.offset()+"\n" +
                            "Timestamp: "+ metadata.timestamp()
                            );
                }
                else
                {
                    logger.info("There is some exception " + e);
                }
            }
        });

        producer.close();
    }
}

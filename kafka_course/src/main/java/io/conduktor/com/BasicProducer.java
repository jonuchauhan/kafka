package io.conduktor.com;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BasicProducer {

    //Create a logger first
    private static final Logger logger = LoggerFactory.getLogger(BasicProducer.class.getName());

    public static String topic ="first_topic";
    public static void main(String[] args) {
        logger.info("Producer creation started");

        logger.info("Step-2 :- setting up properties");

        //setting properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        logger.info("Step-3 :- creating a kafka producer");
        //creating producers
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        //create a producer record
        logger.info("Step-4 :- creating a phone record");
        ProducerRecord<String,String> producerrecord = new
                ProducerRecord<>(topic,"hello kafka");

        //sending a record
        logger.info("Step-5 :- sending producer record");
        producer.send(producerrecord);

        //flush a record
        logger.info("Step-6 :- flushing record");
        producer.flush();

        //close producer
        logger.info("Step-7 :- closing producer");
        producer.close();







    }
}

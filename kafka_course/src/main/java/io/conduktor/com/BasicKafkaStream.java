package io.conduktor.com;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;

import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class BasicKafkaStream {
    private static final Logger log = LoggerFactory.getLogger(BasicConsumer.class.getName());

    public static void main(String[] args) {

        String topic_name="fifth_topic";
        log.info("setting up properties");
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG,"basic");
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass().getName());


        StreamsBuilder streamBuilder = new StreamsBuilder();
        KStream<String ,String> kstream = streamBuilder.stream(topic_name);
        kstream.foreach((k,v)->System.out.println("Key : "+ k +" and value is : "+ v));

        Topology topology = streamBuilder.build();
        KafkaStreams streams = new KafkaStreams(topology,properties);


        streams.start();

        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                log.info("shutdown detected");
                streams.close();
                try {
                        mainThread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }




            }


        });





    }
}

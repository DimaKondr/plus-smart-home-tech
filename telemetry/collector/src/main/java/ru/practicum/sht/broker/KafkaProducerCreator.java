package ru.practicum.sht.broker;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

//@Configuration
public class KafkaProducerCreator {

    //@Bean(destroyMethod = "close")
    public Producer<String, SpecificRecordBase> getProducer() {
        return new KafkaProducer<>(getConfig());
    }

    private Properties getConfig() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "ru.practicum.sht.broker.CollectorAvroSerializer");

        return config;
    }

}
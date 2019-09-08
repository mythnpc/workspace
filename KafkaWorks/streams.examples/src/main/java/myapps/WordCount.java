package main.java.myapps;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import io.confluent.examples.clients.basicavro.Customer;
import io.confluent.examples.clients.basicavro.Payment;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

public class WordCount {

    public static void main(String[] args) throws Exception {
    	System.out.println("Startasd");
   	 // When you want to override serdes explicitly/selectively
	    final Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url",
	                                                                     "http://localhost:8081");
	 // `Foo` and `Bar` are Java classes generated from Avro schemas
	    final Serde<Customer> keySpecificAvroSerde = new SpecificAvroSerde<>();
	    keySpecificAvroSerde.configure(serdeConfig, true); // `true` for record keys
	    final Serde<Payment> valueSpecificAvroSerde = new SpecificAvroSerde<>();
	    valueSpecificAvroSerde.configure(serdeConfig, false); // `false` for record values
    	
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "customer-payment-avro-group");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keySpecificAvroSerde);
	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueSpecificAvroSerde);
	    props.put("schema.registry.url", "http://localhost:8081");
	    

 
        final StreamsBuilder builder = new StreamsBuilder();
 
        KStream<Customer, Payment> source = builder.stream("avro-test", Consumed.with(keySpecificAvroSerde, valueSpecificAvroSerde));
        source.groupByKey(Grouped.with(keySpecificAvroSerde, valueSpecificAvroSerde))
              .count()
              .toStream()
              .peek((key, value) -> System.out.println("key=" + key + ", value=" + value));
 
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);
 
        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
 
        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}

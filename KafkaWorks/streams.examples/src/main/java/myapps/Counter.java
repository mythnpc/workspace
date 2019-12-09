package main.java.myapps;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;



public class Counter {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
 
        System.out.print("hellow");
        
        final StreamsBuilder builder = new StreamsBuilder();
        KeyValueBytesStoreSupplier storeSupplier = Stores.inMemoryKeyValueStore("World");

        KTable<String, String> source = builder.table("database", Materialized.<String, String>as(storeSupplier)
                .withKeySerde(Serdes.String())
                .withValueSerde(Serdes.String()));

        
//        StoreBuilder<KeyValueStore<String, String>> countStoreSupplier =
//      		  Stores.keyValueStoreBuilder(
//      		    Stores.inMemoryKeyValueStore("World"),
//      		    Serdes.String(),
//      		    Serdes.String());
//      		KeyValueStore<String, String> countStore = countStoreSupplier.build();

        


//        source.toStream().peek((key, value) -> System.out.println("key=" + key + ", value=" + value));

        
//        source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
//              .groupBy((key, value) -> value)
//              .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
//              .toStream()
//              .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

        
        final Topology topology = builder.build();
        
        topology.addSource("Source", "extract")
        		.addProcessor("Process", () -> new ReconciliationProcessor(), "Source")
        		.connectProcessorAndStateStores("Process", "World");
        
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
            System.out.println("World");

            latch.await();
//            
//            ReadOnlyKeyValueStore<String, Long> keyValueStore =
//            	    streams.store("World", QueryableStoreTypes.keyValueStore());
//            
//            System.out.println("Hello: " + keyValueStore.get("Key1"));

        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}

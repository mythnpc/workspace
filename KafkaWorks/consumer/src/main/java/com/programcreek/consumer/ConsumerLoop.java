package com.programcreek.consumer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import io.confluent.examples.clients.basicavro.Customer;
import io.confluent.examples.clients.basicavro.Payment;

public class ConsumerLoop implements Runnable {
	  private final KafkaConsumer<Customer, Payment> consumer;
	  private final List<String> topics;
	  private final int id;

	  public ConsumerLoop(int id,
	                      String groupId, 
	                      List<String> topics) {
	    this.id = id;
	    this.topics = topics;
	    Properties props = new Properties();
	    props.put("bootstrap.servers", "localhost:9092");
	    props.put("group.id", groupId);
	    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer");
	    props.put("schema.registry.url", "http://localhost:8081");
	    this.consumer = new KafkaConsumer<Customer, Payment>(props);
	  }
	 
	  @Override
	  public void run() {
	    try {
	      consumer.subscribe(topics);

	      while (true) {
	        ConsumerRecords<Customer, Payment> records = consumer.poll(Duration.ofSeconds(30));
	        for (ConsumerRecord<Customer, Payment> record : records) {
	          Map<String, Object> data = new HashMap<String, Object>();
	          data.put("partition", record.partition());
	          data.put("offset", record.offset());
	          data.put("key", record.key());
	          data.put("value", record.value());
	          System.out.println(this.id + ": " + data);
	        }
	      }
	    } catch (WakeupException e) {
	      // ignore for shutdown 
	    } finally {
	      consumer.close();
	    }
	  }

	  public void shutdown() {
	    consumer.wakeup();
	  }
	}
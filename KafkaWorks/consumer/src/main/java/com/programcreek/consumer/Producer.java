package com.programcreek.consumer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import io.confluent.examples.clients.basicavro.Customer;
import io.confluent.examples.clients.basicavro.Payment;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class Producer {

	private final KafkaProducer<Customer, Payment> producer;
	  
	public Producer() throws UnknownHostException{
		Properties config = new Properties();
		config.put("client.id", InetAddress.getLocalHost().getHostName());
		config.put("bootstrap.servers", "localhost:9092");
		config.put("acks", "all");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
		producer = new KafkaProducer<Customer, Payment>(config);		
	}
	
	public Payment generatePayment(int i){
	    Payment p = new Payment();
	    p.id = "Key" + i;
	    p.amount = 10*i;
	    return p;
	}
	
	public Customer generateCustomer(int i){
		Customer c = new Customer();
		c.id = (i % 2 == 1) ? 1 : 2;
		c.name = (i % 2 == 1) ? "Bob" : "Tom";
		c.age = (i % 2 == 1) ? 27 : 32;

		return c;
	}
	
	public void sendMsg() throws InterruptedException, ExecutionException{

		
		for(int i = 0; i < 5; i++){

		final ProducerRecord<Customer, Payment> record = new ProducerRecord<>("avro-test",generateCustomer(i), generatePayment(i));
		producer.send(record, new Callback() {
			  public void onCompletion(RecordMetadata metadata, Exception e) {
			    if (e != null)
			      System.out.println(e);
			  }
			}).get();
		}
	}
}

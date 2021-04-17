package com.example.coreectconcept;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.xerial.snappy.buffer.DefaultBufferAllocator.factory;

@SpringBootApplication
public class CoreEctConceptApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreEctConceptApplication.class, args);
	}

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name("topic1")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactory(){


		Map<String, Object> config = new HashMap<>();

		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_one");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		return new DefaultKafkaConsumerFactory<String,String>(config);
	}
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setBatchListener(true);
		return factory;
	}

	@KafkaListener(id = "myId", topics = "topic1", containerFactory ="kafkaListenerContainerFactory")

	//public void listen(@Payload List<String> in) { also works here
	public void listen(@Payload List<ConsumerRecord<String, String>> in) {
		System.out.println(in.size());
		for(int i =0; i< in.size(); i++){
			System.out.println("key: " + in.get(i).key() + "value: " + in.get(i).value());
		}
	}
}

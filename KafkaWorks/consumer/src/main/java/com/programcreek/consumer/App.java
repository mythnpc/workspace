package com.programcreek.consumer;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws UnknownHostException, InterruptedException, ExecutionException
    {
    	System.out.println("streams-wordcount-counts-store-changelog");
    	  int numConsumers = 3;
    	  String groupId = "consumer-tutorial-group";
    	  List<String> topics = Arrays.asList("avro-test");
    	  final ExecutorService executor = Executors.newFixedThreadPool(numConsumers);

    	  final List<ConsumerLoop> consumers = new ArrayList<ConsumerLoop>();
    	  for (int i = 0; i < numConsumers; i++) {
    	    ConsumerLoop consumer = new ConsumerLoop(i, groupId, topics);
    	    consumers.add(consumer);
    	    executor.submit(consumer);
    	  }

    	  Runtime.getRuntime().addShutdownHook(new Thread() {
    	    @Override
    	    public void run() {
    	    	System.out.println("Hello");

    	      for (ConsumerLoop consumer : consumers) {
    	        consumer.shutdown();
    	      } 
    	      executor.shutdown();
    	      try {
    	        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
    	      } catch (InterruptedException e) {
    	        e.printStackTrace();
    	      }
    	    }
    	  });
    
    }
}

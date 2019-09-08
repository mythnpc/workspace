package com.programcreek.consumer;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class AppTwo {

	public static void main(String[] args) throws UnknownHostException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
    	System.out.println("Hello");
    	Producer producer = new Producer();
    	producer.sendMsg();
    	System.out.println("Im done");
	}

}

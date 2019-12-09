package main.java.myapps;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.ValueAndTimestamp;

public class ReconciliationProcessor implements Processor<String, String> {

	  private ProcessorContext context;
	  private KeyValueStore<String, ValueAndTimestamp> kvStore;

	  @Override
	  @SuppressWarnings("unchecked")
	  public void init(ProcessorContext context) {
	      // keep the processor context locally because we need it in punctuate() and commit()
	      this.context = context;

	      // retrieve the key-value store named "Counts"
	      kvStore = (KeyValueStore<String,ValueAndTimestamp>) context.getStateStore("World");

////	      // schedule a punctuate() method every 1000 milliseconds based on stream-time
//	      this.context.schedule(1000, PunctuationType.STREAM_TIME, (timestamp) -> {
//	          KeyValueIterator<String, Long> iter = this.kvStore.all();
//	          while (iter.hasNext()) {
//	              KeyValue<String, Long> entry = iter.next();
//	              context.forward(entry.key, entry.value.toString());
//	          }
//	          iter.close();
//
//	          // commit the current processing progress
//	          context.commit();
//	      });
	  }

	  @Override
	  public void close() {
	      // close any resources managed by this processor
	      // Note: Do not close any StateStores as these are managed by the library
	  }

	@Override
	public void process(String key, String value) {
		// TODO Auto-generated method stub
		System.out.println("Key: " + key + " Value: " + value);
		System.out.println(kvStore.get("Key3").value());
	}

	}
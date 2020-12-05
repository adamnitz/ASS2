package bgu.spl.mics;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	
	private static MessageBusImpl INSTANCE = null;
	LinkedBlockingQueue<Message> messages;
	HashMap<MicroService, LinkedBlockingQueue> myMap;


	private MessageBusImpl (){
		myMap = new HashMap();
		messages = new LinkedBlockingQueue<Message>();
	}

	public static MessageBusImpl getInstance(){
		if(INSTANCE ==null)
			INSTANCE = new MessageBusImpl();
		return INSTANCE;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		
        return null;
	}

	@Override
	public void register(MicroService m) {
		myMap.put(m,new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		myMap.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}
}

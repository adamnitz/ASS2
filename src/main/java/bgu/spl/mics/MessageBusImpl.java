package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private int counter=0; //counter for roundRubin
	private static MessageBusImpl INSTANCE = null;
	LinkedBlockingQueue<Message> messages;
	HashMap<MicroService, LinkedBlockingQueue<Message>> mapQueue; // messages for each microService
	HashMap<Class<? extends Event<Boolean>> ,Vector<MicroService>> typeMessage; // typeMessage for each microService
	HashMap<MicroService, Future<Boolean>> calculation; //update futures

	private MessageBusImpl (){
		mapQueue = new HashMap();
		messages = new LinkedBlockingQueue<Message>();
	}

	public static MessageBusImpl getInstance(){
		if(INSTANCE ==null)
			INSTANCE = new MessageBusImpl();
		return INSTANCE;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m)
	{
		if(typeMessage.get(type)== null)
		{
			Vector<MicroService> whichMicro = new Vector<MicroService>();
			typeMessage.put((Class<? extends Event<Boolean>>) type,whichMicro);
		}

		if(!typeMessage.get(type).contains(m))
			typeMessage.get(type).add(m);



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
		messages.add(e);

		Message msg = messages.remove();
		Vector<MicroService> microVec = typeMessage.get(AttackEvent.class);

		if(msg.getClass()==AttackEvent.class)
		{
			if(counter==microVec.size())
				counter = 0;
			 if (counter<microVec.size()) {

				MicroService micro = microVec.get(counter);
				mapQueue.get(micro).add(msg);
				counter ++;
			}
		}

        return null;
	}

	@Override
	public void register(MicroService m) {
		mapQueue.put(m,new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		mapQueue.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if(!mapQueue.containsKey(m))
			throw new IllegalStateException();
		while (mapQueue.get(m).isEmpty())
			wait();

		Message msg= mapQueue.get(m).remove();


		return msg;
	}
}

package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;


import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private static class MessageBusImplHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	public static MessageBusImpl getInstance(){

		return MessageBusImplHolder.instance;
	}

	private int counter; //counter for roundRubin
	private int totalAttacks; //count total attacks of han solo and 3cpo

	LinkedBlockingQueue<Message> messages;
	ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> mapQueue; // messages for each microService
	ConcurrentHashMap<Class <? extends Message> ,Vector<MicroService>> typeMessage; // typeMessage for each microService
	public ConcurrentHashMap<Event, Future<Boolean>> futureMap; //update futures
	public ConcurrentHashMap<Class<? extends Message>, Callback> callMap; //check the type in callBack

	private MessageBusImpl (){
		messages = new LinkedBlockingQueue<Message>();
		mapQueue = new ConcurrentHashMap<>();
		typeMessage = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
		callMap = new ConcurrentHashMap<>();
		totalAttacks =0;
		counter=0;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m)
	{

		if(typeMessage.get(type)== null)
		{
			Vector<MicroService> whichMicro = new Vector<MicroService>();
			typeMessage.put( type,whichMicro);
		}

		if(!typeMessage.get(type).contains(m))
			typeMessage.get(type).add(m);

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(typeMessage.get(type)== null)
		{
			Vector<MicroService> whichMicro = new Vector<MicroService>();
			typeMessage.put(type,whichMicro);
		}

		if(!typeMessage.get(type).contains(m))
			typeMessage.get(type).add(m);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		futureMap.get(e).resolve((Boolean) result);
	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		messages.add(b);
		Message msg = messages.remove();

		Vector<MicroService> micro = typeMessage.get(msg.getClass());
		for(int i=0; i <micro.size();i++)
		{
			mapQueue.get(micro.get(i)).add(msg);
		}

		notifyAll();
	}

	
	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		messages.add(e);

		while(!this.mapQueue.containsKey(e.getClass())) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
		}

		Message msg = messages.remove();
		Future future = new Future();

		futureMap.put(e,future);

		if(msg.getClass()==AttackEvent.class)
		{

			Vector<MicroService> microVec = typeMessage.get(AttackEvent.class);
			if(counter==microVec.size())
				counter = 0;
			 if (counter<microVec.size()) {
				MicroService micro = microVec.get(counter);
				mapQueue.get(micro).add(msg);
				counter ++;
			 }
			 totalAttacks++;
		}

		else
		{
			Vector<MicroService> micro = typeMessage.get(msg.getClass());
			for(int i=0; i <micro.size();i++)
			{
				mapQueue.get(micro.get(i)).add(msg);
			}
		}


		notifyAll();
        return (Future<T>) futureMap.get(msg.getClass());
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
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
		if(!mapQueue.containsKey(m))
			throw new IllegalStateException();
		while (mapQueue.get(m).isEmpty())
		{
			this.wait();
		}

		Message msg= mapQueue.get(m).remove();


		return msg;
	}
}

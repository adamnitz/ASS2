package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;


import java.util.HashMap;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
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


	private ConcurrentLinkedQueue<Message> messages;
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> mapQueue; // messages for each microService
	private ConcurrentHashMap<Class <? extends Message> ,LinkedBlockingQueue<MicroService>> typeMessage; // typeMessage for each microService
	private ConcurrentHashMap<Event, Future> futureMap; //update futures
	//public ConcurrentHashMap<Class<? extends Message>, Callback> callMap; //TODO:needs to be in microservice

	private MessageBusImpl (){
		messages = new ConcurrentLinkedQueue<Message>();
		mapQueue = new ConcurrentHashMap<>();
		typeMessage = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m)
	{

		LinkedBlockingQueue mQue = typeMessage.get(type);

		if(mQue == null)
		{
			LinkedBlockingQueue whichMicro = new LinkedBlockingQueue<MicroService>();
			typeMessage.put(type,whichMicro);
		}

		else if(!mQue.contains(m))
		{
			mQue.add(m);
		}

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {

		LinkedBlockingQueue mQue = typeMessage.get(type);

		if(mQue == null)
		{
			LinkedBlockingQueue whichMicro = new LinkedBlockingQueue<MicroService>();
			typeMessage.put(type,whichMicro);
		}

		else if(!mQue.contains(m))
		{
			mQue.add(m);
		}

	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		futureMap.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {

		messages.add(b);

		Message msg = messages.remove();

		LinkedBlockingQueue mQue = typeMessage.get(msg);

		if(mQue!=null)
		{
			for(int i =0 ; i<mQue.size(); i ++)
			{
				mapQueue.get(i).add(msg);
			}
		}


		/*TODO:needTosynchronize
		TODO:notifyAll
		 */
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {

		messages.add(e);

		Message msg = messages.remove();

		/*TODO:
		while(!this.mapQueue.containsKey(e.getClass())) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}

			//OR CountDownLatch
		 */

		LinkedBlockingQueue<MicroService> mQue = typeMessage.get(msg);
		MicroService first =  mQue.poll();

		mapQueue.get(first).add(msg);
		try {
			mQue.put(first);
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}

		Future future = new Future();
		futureMap.put(e,future);

		/*Todo:
		synchronized (msQ) {
			msQ.put(msg);
			msQ.notifyAll();
		 */

		return future;


	}


	@Override
	public void register(MicroService m) {
		mapQueue.put(m,new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {

		LinkedBlockingQueue remQue = mapQueue.remove(m);

		if(remQue!=null)
		{
			for(int i=0; i< remQue.size(); i ++)
			{
				remQue.remove();
			}
		}

	}

	@Override
	public  Message awaitMessage(MicroService m) throws InterruptedException {
		LinkedBlockingQueue msQ = mapQueue.get(m);

		synchronized (msQ)
		{
			while (msQ.isEmpty())
			{

				try {
					msQ.wait();
				} catch (InterruptedException e){}

			}
			Message msg= mapQueue.get(m).remove();
			return msg;
		}
	}
}

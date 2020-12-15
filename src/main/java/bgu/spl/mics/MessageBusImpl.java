package bgu.spl.mics;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus{
    private static class MessageBusImplHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }
    public static MessageBusImpl getInstance() {
        return MessageBusImplHolder.instance;
    }


    private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> mapQueue; // messages for each microService
    private ConcurrentHashMap<Class<? extends Message>, LinkedBlockingQueue<MicroService>> typeMessage; // typeMessage for each microService
    private ConcurrentHashMap<Event, Future> futureMap; //update futures
    private static final Object lock = new Object();

    private MessageBusImpl() {
        mapQueue = new ConcurrentHashMap<>();
        typeMessage = new ConcurrentHashMap<>();
        futureMap = new ConcurrentHashMap<>();
    }


    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        synchronized (typeMessage) {
            if (typeMessage.get(type) == null) {
                LinkedBlockingQueue whichMicro = new LinkedBlockingQueue<MicroService>();
                typeMessage.put(type, whichMicro);
            }
        }
        LinkedBlockingQueue mQue = typeMessage.get(type);
        synchronized (mQue) {
            if (!mQue.contains(m)) {
                mQue.add(m);
            }
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        synchronized (typeMessage) {
            if (typeMessage.get(type) == null) {
                LinkedBlockingQueue whichMicro = new LinkedBlockingQueue<MicroService>();
                typeMessage.put(type, whichMicro);
            }
        }
        LinkedBlockingQueue mQue = typeMessage.get(type);
        synchronized (mQue) {
            if (!mQue.contains(m)) {
                mQue.add(m);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
            futureMap.get(e).resolve(result);

    }

    @Override
    public void sendBroadcast(Broadcast b) {
        LinkedBlockingQueue<MicroService> eMicro = typeMessage.get(b.getClass());
        int i=eMicro.size();
        while(i>0) {
            LinkedBlockingQueue<Message> currentMSQ = mapQueue.get(eMicro.poll());
            synchronized (currentMSQ) {
                currentMSQ.add(b);
                currentMSQ.notifyAll();
            }
            i--;
        }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        LinkedBlockingQueue<MicroService> eMicro = typeMessage.get(e.getClass());
        MicroService firstMicro = null;
        if(eMicro == null)
            return null;

        synchronized (eMicro){
            if(eMicro.size()==0)
                return null;
            firstMicro = eMicro.poll();
            eMicro.add(firstMicro);//roundRubin
        }

        LinkedBlockingQueue<Message> QforMs  = mapQueue.get(firstMicro);//addMsg

        synchronized (QforMs) {
            try{
                QforMs.put(e);
            } catch(InterruptedException exe){}
            QforMs.notifyAll();
            Future future = new Future();
                futureMap.put(e, future);
            return future;
        }

 }




    @Override
    public void register(MicroService m){
        synchronized (mapQueue){
            mapQueue.put(m, new LinkedBlockingQueue<Message>());
        }
    }

    @Override
    public void unregister(MicroService m) {
        synchronized (typeMessage) {
                typeMessage.forEach((msg, microQue) -> {
                synchronized (microQue) {
                    microQue.remove(m);
                }
            });}
            synchronized (mapQueue) {
                mapQueue.remove(m);
            }
        }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        LinkedBlockingQueue<Message> msQ = mapQueue.get(m);
        if(msQ==null) {
             throw new RuntimeException("");//TODO: make sure what should we have here
        }

        synchronized (msQ){
            while (msQ.isEmpty()){
                try{
                    msQ.wait();
                }catch (InterruptedException e) {}
            }

            Message msg = msQ.remove();
            return msg;
        }
    }
}

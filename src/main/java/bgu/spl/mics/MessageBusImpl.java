package bgu.spl.mics;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private static class MessageBusImplHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    public static MessageBusImpl getInstance() {

        return MessageBusImplHolder.instance;
    }


    //private ConcurrentLinkedQueue<Message> messages;
    private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> mapQueue; // messages for each microService
    private ConcurrentHashMap<Class<? extends Message>, LinkedBlockingQueue<MicroService>> typeMessage; // typeMessage for each microService
    private ConcurrentHashMap<Event, Future> futureMap; //update futures


    private MessageBusImpl() {
        //messages = new ConcurrentLinkedQueue<Message>();
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

        System.out.println("subscribeEvent");

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

        //System.out.println("suscribe Broadcast");


    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        futureMap.get(e).resolve(result);

        System.out.println("complete");


    }

    @Override
    public void sendBroadcast(Broadcast b) {
        LinkedBlockingQueue eMicro = typeMessage.get(b.getClass());
        //System.out.println(b+"what the hell");
        //Sy//stem.out.println(eMicro.size()+ "emicro");

            //if(eMicro.size()==0) throw new NullPointerException();/*return null*/;
            for (int i = 0; i < eMicro.size(); i++) {
                LinkedBlockingQueue<Message> currentMSQ=mapQueue.get(eMicro.poll());
                synchronized ( currentMSQ){
                    currentMSQ.add(b);
                    currentMSQ.notifyAll();

            }
        }


        System.out.println("sent Broadcast");

    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        LinkedBlockingQueue<MicroService> eMicro = typeMessage.get(e.getClass());

        //Pull first MS from eMicro, store, push back to the eMicro Q with roundRubin
        MicroService firstMicro = null;
        synchronized (eMicro)
        {
            if(eMicro.size()==0) return null;
            firstMicro = eMicro.poll();
           eMicro.add(firstMicro);//roundRubin
        }
        //Pull relevant Q of messages for Ms, sync and add the message, notifall all pending for this q.
        LinkedBlockingQueue<Message> QforMs  = mapQueue.get(firstMicro);//addMsg
        synchronized (QforMs) {
            try{
                QforMs.put(e);
            } catch(InterruptedException exe){}
            QforMs.notifyAll();
        }
        //Create matching future for the event only if it was sent to someone and add to futureMap.
        Future future = new Future();
        futureMap.put(e, future);

        return future;

    }


    @Override
    public void register(MicroService m) {
        synchronized (mapQueue) {
            mapQueue.put(m, new LinkedBlockingQueue<Message>());
        }

        System.out.println("register");

    }

    @Override
    public void unregister(MicroService m) {
        synchronized (mapQueue) {
            LinkedBlockingQueue remQue = mapQueue.remove(m);

            if (remQue != null) {
                for (int i = 0; i < remQue.size(); i++) {
                    remQue.remove();
                }
            }
        }

        //TODO:NEED TO CHANGE THE MICRO REFERENCES

        System.out.println("unregister");


    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        LinkedBlockingQueue msQ = mapQueue.get(m);

        if(msQ==null) {
             throw new RuntimeException("");

        }
        synchronized (msQ) {

            System.out.println(mapQueue.get(m).size() + "how many messages in m q");

            while (msQ.isEmpty()) {
                try {

                    msQ.wait();

                } catch (InterruptedException e) {
                }

            }
            Message msg = mapQueue.get(m).remove();
            //System.out.println(msg+" ?????????????????");
            return msg;
        }

    }
}

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


    private ConcurrentLinkedQueue<Message> messages;
    private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> mapQueue; // messages for each microService
    private ConcurrentHashMap<Class<? extends Message>, LinkedBlockingQueue<MicroService>> typeMessage; // typeMessage for each microService
    private ConcurrentHashMap<Event, Future> futureMap; //update futures


    private MessageBusImpl() {
        messages = new ConcurrentLinkedQueue<Message>();
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

        messages.add(b);

        Message msg = messages.remove();

        if (!this.typeMessage.containsKey(b.getClass())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        LinkedBlockingQueue mQue = typeMessage.get(msg);

        synchronized (mQue) {
            for (int i = 0; i < mQue.size(); i++) {
                mapQueue.get(i).add(msg);
            }
            mQue.notifyAll();
        }

        System.out.println("sent Broadcast");


    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        messages.add(e);

        Message msg = messages.remove();

        if (!this.typeMessage.containsKey(e.getClass())) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        //OR CountDownLatch
        LinkedBlockingQueue<MicroService> mQue = typeMessage.get(msg.getClass());
        synchronized (mQue) {
            MicroService first = mQue.poll();
            first = mQue.poll();
            mapQueue.get(first).add(msg);
            try {
                mQue.put(first);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            synchronized (typeMessage) {
                typeMessage.get(msg.getClass()).add(first);

            }
            mQue.notifyAll();
            System.out.println(mapQueue.get(first).toString()+"send event = im not empty?");
        }

        Future future = new Future();
        futureMap.put(e, future);

        System.out.println(Thread.currentThread().getName());




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
             try {
                Thread.sleep(50);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }


        }
        synchronized (msQ) {

            System.out.println(mapQueue.get(m).toString()+"await messsage = im empty");

            while (msQ.isEmpty()) {
                try {

                    msQ.wait();

                } catch (InterruptedException e) {
                }

            }
            Message msg = mapQueue.get(m).remove();
            return msg;
        }

       // System.out.println("AwaitMessage");
    }
}

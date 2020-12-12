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

        System.out.println("suscribe Broadcast");


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

        LinkedBlockingQueue mQue = typeMessage.get(msg);

        if (mQue != null) {
            for (int i = 0; i < mQue.size(); i++) {
                mapQueue.get(i).add(msg);
            }
        }

        System.out.println("sent Broadcast");

        /*TODO:needTosynchronize?*/
        notifyAll();

    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {

        messages.add(e);

        Message msg = messages.remove();

        System.out.println("check1");
        //TODO:CHECK
        while (!this.mapQueue.containsKey(e.getClass())) {
            System.out.println("check2");
            try {
                System.out.println("check3");
                Thread.sleep(50);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        //OR CountDownLatch


        LinkedBlockingQueue<MicroService> mQue = typeMessage.get(msg);
        synchronized (mQue) {
            MicroService first = mQue.poll();

            mapQueue.get(first).add(msg);
            try {
                mQue.put(first);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        Future future = new Future();
        futureMap.put(e, future);


        notifyAll();



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

        System.out.println(msQ+"check...............");

        synchronized (msQ) {
            while (msQ.isEmpty()) {
                    System.out.println("inwhile");
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

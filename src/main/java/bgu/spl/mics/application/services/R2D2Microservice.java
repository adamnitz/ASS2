package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {

    private MessageBus msgBus;
    private long duration;


    public R2D2Microservice(long duration, MessageBus messageBus)
    {
        super("R2D2");
        this.msgBus = messageBus;
        this.duration = duration;
    }


    protected void initialize() {

        Diary d = Diary.getInstance();
        subscribeEvent(DeactivationEvent.class,(event)-> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sendEvent(new BombDestroyerEvent());
            d.setR2D2Deactivate();
        });
        subscribeBroadcast(TerminationBroadcast.class, (e) -> {d.setR2D2Terminate();});

    }

}
package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
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

    private long duration;

    /**Constructor
     * @param duration - the duration of the bombDestroy
     */
    public R2D2Microservice(long duration)
    {
        super("R2D2");
        this.duration = duration;
    }

    /**
     * suscribe R2D2 to R2D2DeactivationEvent and termination broadcast
     * define the callBacks of those messages:
     * * DeactivationEvent:sleep(execute the attack)
     * * terminationBroadcast: set R2D2 Termination time
     */
    protected void initialize() {

        Diary d = Diary.getInstance();
        subscribeEvent(DeactivationEvent.class,(event)-> {
            try {
                Thread.sleep(duration);
                complete(event, true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            d.setR2D2Deactivate();
        });

        subscribeBroadcast(TerminationBroadcast.class, (e) -> {d.setR2D2Terminate();
            terminate();
        });

    }

}
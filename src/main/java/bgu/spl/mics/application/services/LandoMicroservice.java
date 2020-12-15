package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;


/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService  {

    private long duration;


    /**Constructor
     * @param duration - the duration of the bombDestroy
     */
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    /**
     * suscribe lando to BrombDestroyerEvent and termination broadcast
     * define the callBacks of those messages:
     *      * BombDestroyerEvent:sleep(execute the attack)
     *      * terminathionBroadcast: set lando Termination time
     */
    protected void initialize() {
       subscribeEvent(BombDestroyerEvent.class,(event)->{
           try {
               Thread.sleep(duration);
               System.out.println("check");
               complete(event, true);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       } );

        subscribeBroadcast(TerminationBroadcast.class, (e) ->
        {d.setLandoTerminate();
            terminate();
        });
    }


}

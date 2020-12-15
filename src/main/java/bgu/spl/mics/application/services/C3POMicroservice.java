package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;


import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService  {
    /**
     * Constructor
     */
    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    /**
     * suscribe 3cpo to attackEvent and termination broadcast
     * define the callBacks of those messages:
     * AttackEvent: check the needed ewoks availability and aquired them,
     * sleep(execute the attack)
     * when he finish, he realeses the ewoks.
     * terminathionBroadcast: set 3cpo Termination time
     */
    protected void initialize() {
            subscribeEvent(AttackEvent.class,(event)-> {
                System.out.println("3cpo startedAttack:" +event.getAttack().getSerials());
                Attack attack = event.getAttack();
            List<Integer> serials = attack.getSerials();
            Vector<Ewok> ewoks = Ewoks.getInstance().getEwoks();

            //checks ewoks availability
                boolean found = false;
                int j=0;
                int i=0;

              //  synchronized (ewoks) {
                    while (i < ewoks.size()) {
                        while (j < serials.size() && !found) {
                            if (serials.get(j) == i + 1) {
                                //  synchronized (ewoks.get(i)) {
                                ewoks.get(i).acquire();
                                // }
                                found = true;
                                j++;
                            }
                            i++;
                        }

                        if(!found)
                            i++;
                        else
                            found = false;

                    }

                System.out.println("test");

                try {
                    Thread.sleep(event.getAttack().getDuration());
                    complete(event, true);
                  //  ewoks.notifyAll();
                    System.out.println("3cpo finish attack");
                    d.setTotalAttack();

                } catch (InterruptedException e) {
                }
                //release the used ewoks
                for(int k=0; k<serials.size(); k++){
                    System.out.println("try to release");
                    ewoks.get(serials.get(k)-1).release();
                }
                d.setC3POFinish();

            });

        subscribeBroadcast(TerminationBroadcast.class, (e) ->
        {d.setC3POTerminate();
        terminate();});
        d.setC3POFinish();
    }
}

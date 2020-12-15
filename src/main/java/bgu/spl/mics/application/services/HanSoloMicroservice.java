package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;


import java.util.Comparator;
import java.util.List;
import java.util.Vector;


/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    /**
     * Constructor
     */
    public HanSoloMicroservice() {
        super("Han");
    }

    @Override
    /**
     * suscribe HanSolo to attackEvent and termination broadcast
     * define the callBacks of those messages:
     * AttackEvent: check the needed ewoks availability and aquired them,
     * sleep(execute the attack)
     * when he finish, he realeses the ewoks.
     * terminathionBroadcast: set HanSolo Termination time
     */
    protected void initialize() {
        subscribeEvent(AttackEvent.class,(event)-> {
            Attack attack = event.getAttack();
            List<Integer> serials = attack.getSerials();
            Vector<Ewok> ewoks = Ewoks.getInstance().getEwoks();

            boolean found = false;
            int j=0;
            int i=0;

            while (i < ewoks.size()) {
                    while (j < serials.size() && !found) {
                        if (serials.get(j) == i + 1) {
                            ewoks.get(i).acquire();

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


            try {
                Thread.sleep(event.getAttack().getDuration());
                complete(event, true);
                d.setTotalAttack();
            }
            catch (InterruptedException e) {}

            //release the used ewoks
            for(int k=0; k<serials.size(); k++){
                ewoks.get(serials.get(k)-1).release();
            }
            d.setHanSoloFinish();
        });

        subscribeBroadcast(TerminationBroadcast.class, (e) ->
        {d.setHanSoloTerminate();
            terminate();});
        d.setHanSoloFinish();// check if here
    }
}

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

    public HanSoloMicroservice() {
        super("Han");
    }


    @Override
    protected void initialize() {


        subscribeEvent(AttackEvent.class,(event)-> {

            Attack attack = event.getAttack();

            List<Integer> serials = attack.getSerials();
            Vector<Ewok> ewoks = Ewoks.getInstance().getEwoks();
            // checks ewoks availability

            serials.sort(Comparator.comparing(Integer::intValue));
            for(int i=0; i<ewoks.size(); i++)
            {
               // System.out.println("check for");
                if(serials.get(i) == i+1)
                {
                    if (ewoks.get(i).getAvailable() == true)
                    {
                        ewoks.get(i).acquire();//for how long
                    }
                    else
                    {
                        try{
                            this.wait();
                        }
                        catch(InterruptedException e){}
                    }
                }
            }
            try {
                Thread.sleep(event.getAttack().getDuration());
                complete(event, true);
                d.setTotalAttack();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //release the used ewoks
            for(int i=0; i<serials.size(); i++)
            {

                ewoks.get(i).release();
            }


            //notifyAll();
            d.setC3POFinish();


        });

        subscribeBroadcast(TerminationBroadcast.class, (e) -> {d.setHanSoloTerminate();
            terminate();
        });

        d.setHanSoloFinish();
    }
}

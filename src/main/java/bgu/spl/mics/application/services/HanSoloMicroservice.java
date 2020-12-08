package bgu.spl.mics.application.services;


import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;


/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    private MessageBus msgBus;


    public HanSoloMicroservice(MessageBus messageBus) {

        super("Han");
        this.msgBus = messageBus;
    }


    @Override
    protected void initialize() {
        msgBus.subscribeEvent(AttackEvent.class,(event)-> {
        Attack attack = event.getAttack();
        List<Integer> serials = attack.getSerials();
        Ewoks ewoks = Ewoks.getInstance();

        for(int i=0; i<ewoks.getEwoks().size(); i++)
        {
            for(int j=0; j< serials.size(); j++)
            {
                if(serials.get(j) == i+1)
                {
                    if(ewoks.getEwoks().get(i).getAvailable()== true)
                    {
                        ewoks.getEwoks().get(i).acquire();//for how long
                    }
                    else
                        Thread.wait();

                }
            }

        }

        Thread.sleep(event.getDuration());
    });
    }

}

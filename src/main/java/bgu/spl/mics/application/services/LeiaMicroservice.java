package bgu.spl.mics.application.services;


import java.util.Vector;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;



import javax.security.auth.callback.Callback;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService implements Callback {

    private Attack[] attacks;//check because of the main
    Vector<Future> futArr;
    int counterAttack;


    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        futArr = new Vector<Future>();
        counterAttack=0;
    }

    @Override
    protected void initialize() {


        subscribeBroadcast(TerminationBroadcast.class, (e) -> {
            d.setLeiaTerminate();
        });

        System.out.println("lia subscrive,");

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {}

        for (int i = 0; i < attacks.length; i++) {
            Future future = sendEvent(new AttackEvent(attacks[i]));
            futArr.add(i,future);
            System.out.println("The Event had sent");

        }

        // System.out.println("befor deactivation");
        for (int i = 0; i < futArr.size(); i++) {
            System.out.println("befor deactivation");

            futArr.get(i).get(); //check if all the attacks had finished
            counterAttack++;


        }
        d.setTotalAttack(totalAttacks);

            sendEvent(new DeactivationEvent());

            sendEvent(new BombDestroyerEvent());
            sendBroadcast(new TerminationBroadcast());

        terminate();

    }
}


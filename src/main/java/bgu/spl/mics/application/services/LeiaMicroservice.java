package bgu.spl.mics.application.services;


import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import javax.security.auth.callback.Callback;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 *
 * Leia is responsible to run the Events in the program.
 */


public class LeiaMicroservice extends MicroService implements Callback {
    private Attack[] attacks;//check because of the main
    Vector<Future> futArr;
    AtomicInteger attackSize;


    /**
     * Constructor-
     * futArr- status of the attacks
     */
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        futArr = new Vector<Future>();
        attackSize=new AtomicInteger(0);
    }


    /**
     *  * suscribe leia to termination broadcast
     *      * define the callBacks of those messages:
     *       * terminathionBroadcast: set 3cpo Termination time
     *  *send the attacksEvent
     *  *check if all the attacks are finishing
     *  *if so, send to R2D2 DeactivationEvents
     *  *if finished, send to Lando BombDestroyerEvent
     *  send TerminationBroadcast
     */

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminationBroadcast.class, (e) -> {
            d.setLeiaTerminate();
            terminate();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        for (int i = 0; i < attacks.length; i++){
            Future future= sendEvent(new AttackEvent(attacks[i]));
            futArr.add(i,future);
        }

        for (int i = 0; i < futArr.size(); i++) {
            futArr.elementAt(i).get(); //check if all the attacks had finished
       }

            Future deactivateFuture = sendEvent(new DeactivationEvent());
            deactivateFuture.get();
            Future bombFuture = sendEvent(new BombDestroyerEvent());
            bombFuture.get();
            sendBroadcast(new TerminationBroadcast());
    }

    public void setAttacksize(int length) {
        this.attackSize.set(length);
    }
}


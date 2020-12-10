package bgu.spl.mics.application.services;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import jdk.jfr.internal.EventWriterMethod;


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


    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminationBroadcast.class, (e) -> {d.setLeiaTerminate();});

            for (int i = 0; i < attacks.length; i++)
                sendEvent((AttackEvent)attacks[i]);


            for (int i = 0; i < msgBus.futureMap.size(); i++) {
                msgBus.futureMap.get(i).get();
            }

            DeactivationEvent event = new DeactivationEvent();
            sendEvent(event);

        }



}


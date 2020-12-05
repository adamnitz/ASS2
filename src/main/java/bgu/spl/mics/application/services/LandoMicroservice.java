package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private MessageBus msgBus;


    public LandoMicroservice(long duration,MessageBus messageBus) {
        super("Lando");
        this.msgBus = messageBus;

    }

    @Override
    protected void initialize() {
       msgBus.register(this);
       msgBus.subscribeEvent(BombDestroyerEvent.class, this);
    }

    public void callBack(){

    }
}

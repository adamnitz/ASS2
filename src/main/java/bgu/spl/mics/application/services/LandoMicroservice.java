package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;

//import javax.security.auth.callback.Callback;//check

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService implements Callback {

    private MessageBus msgBus;


    public LandoMicroservice(long duration,MessageBus messageBus) {
        super("Lando");
        this.msgBus = messageBus;

    }

    @Override
    protected void initialize() {
       msgBus.subscribeEvent(BombDestroyerEvent.class, this);
    }

    public void callBack(){

    }
}

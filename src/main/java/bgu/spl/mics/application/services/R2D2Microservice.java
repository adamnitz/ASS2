package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;

import javax.security.auth.callback.Callback;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService implements Callback {

    private MessageBus msgBus;


    public R2D2Microservice(long duration, MessageBus messageBus)
    {
        super("R2D2");
        this.msgBus = messageBus;
    }

    @Override
    protected void initialize() {
        msgBus.register(this);
        msgBus.subscribeEvent(DeactivationEvent.class,this);
    }

    public void callBack(){

    }
}

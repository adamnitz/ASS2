package bgu.spl.mics.application.services;

import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;

import javax.security.auth.callback.Callback;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService implements Callback {

    private MessageBus msgBus;


    public C3POMicroservice(MessageBus messageBus) {
        super("C3PO");
        this.msgBus = messageBus;
    }

    @Override
    protected void initialize() {
        msgBus.register(this);
        msgBus.subscribeEvent(AttackEvent.class,this);
        ()->{retur}

    }

    public void callBack(){
        Message msg=  msgBus.awaitMessage(this); //needs to be in run
        msg.

    }
}

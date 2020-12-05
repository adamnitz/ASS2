package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private MessageBus msgBus;
	
    public LeiaMicroservice(Attack[] attacks, MessageBus messageBus) {
        super("Leia");
		this.attacks = attacks;
        msgBus = messageBus;
    }

    @Override
    protected void initialize() {
        msgBus.register(this);
    }
}

package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent extends Attack implements Event<Boolean>  {


    /**
     * Constructor.
     *
     * @param serialNumbers
     * @param duration
     */
    public AttackEvent(List<Integer> serialNumbers, int duration) {
        super(serialNumbers, duration);
    }
}


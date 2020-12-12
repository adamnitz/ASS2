package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.text.AttributedString;
import java.util.List;

public class AttackEvent implements Event<Boolean>  {
    private Attack attack;

    /**
     * Constructor.
     *
     * @param serialNumbers
     * @param duration
     */
    public AttackEvent(Attack attack) {
        this.attack = attack;
    }

    public Event<Boolean> getEvent()
    {
        return this;
    }

    public Attack getAttack()
    {
        return attack;
    }


}


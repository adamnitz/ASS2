package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DeactivationEvent implements Event<Boolean> {

    private int duration;

    public DeactivationEvent(int duration)
    {
        this.duration = duration;
    }

    public int getDuration()
    {
        return duration;
    }

    public Event<Boolean> getEvent()
    {
        return this;
    }

}

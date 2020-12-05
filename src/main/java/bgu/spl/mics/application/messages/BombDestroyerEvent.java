package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {

    private int duration;

    public BombDestroyerEvent(int duration)
    {
        this.duration = duration;
    }

    public int getDuration()
    {
        return duration;
    }
}

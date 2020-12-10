package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {

    public Event<Boolean> getEvent()
    {
        return this;
    }
}

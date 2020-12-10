package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DeactivationEvent implements Event<Boolean> {


    @Override
    public Event<Boolean> getEvent() {
        return this;
    }
}

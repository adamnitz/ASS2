package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;

import java.util.Vector;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    private Vector <Ewok> ewoks = new Vector<Ewok>();
    private static Ewoks INSTANCE = null;

    public Ewoks(int numOfEwoks) {
       for(int i=0; i<numOfEwoks;i++)
       {
           ewoks.add(new Ewok(i+1));
       }
    }


    public static Ewoks getInstance(){
        if(INSTANCE ==null)
            INSTANCE = new Ewoks(0);
        return INSTANCE;
    }




}

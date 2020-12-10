package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.application.passiveObjects.Input;

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

    private Vector <Ewok> ewoks;

    private static class EwoksHolder{

        private static Ewoks instance = new Ewoks();
    }

    private Ewoks() {
        ewoks = new Vector<>();

    }

    public void setEwoks(int numOfEwoks) {
        for(int i=0; i<numOfEwoks;i++)
        {
            ewoks.add(new Ewok(i+1));
        }
    }

    public static Ewoks getInstance(){
      return EwoksHolder.instance;
    }

    public Vector<Ewok> getEwoks() {
        return ewoks;
    }





}

package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	int serialNumber;
	boolean available;
	
    public Ewok(int serialNumber)
    {
        this.serialNumber = serialNumber;
        this.available = true;
    }
    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
        if (available)
		    available =false;
        else {
            if(!available) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * release an Ewok
     */
    public synchronized void release() {
    	available=true;
    	notifyAll();
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public boolean getAvailable()
    {
        return available;
    }


}

package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	private int serialNumber;
	 boolean available;

    /**Constructor
     *
     * @param serialNumber - the serial number of the ewok
     * available - tells if the ewok is available or not
     */
    public Ewok(int serialNumber)
    {
        this.serialNumber = serialNumber;
        this.available = true;
    }
    /**
     * Acquires an Ewok if he is available
     * else wait until it becames available
     */
    public synchronized void acquire() {
            while(!available) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }
            available =false;


    }

    /**
     * release an Ewok after we using him
     */
    public synchronized void release() {
    	available=true;
    	notifyAll();
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public boolean getAvailable() {
        return available;
    }
}

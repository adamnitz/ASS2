package bgu.spl.mics.application.passiveObjects;

import java.util.Comparator;
import java.util.List;


/**
 * Passive data-object representing an attack object.
 * You must not alter any of the given public methods of this class.
 * <p>
 * YDo not add any additional members/method to this class (except for getters).
 */
public class Attack {
    final List<Integer> serials;
    final int duration;

    /**
     * Constructor Attack
     * SerialNumbers - the Ewoks we need
     * duration - the duration of TheAttack
     */
    public Attack(List<Integer> serialNumbers, int duration) {
        serialNumbers.sort(Comparator.naturalOrder());
        this.serials = serialNumbers;
        this.duration = duration;
    }

    /** get the Serials
     *
     * @return serials
     */
    public List<Integer> getSerials()
    {
        return serials;
    }

    /** get The duration
     *
     * @return serials
     */
    public int getDuration()
    {
        return duration;
    }

}

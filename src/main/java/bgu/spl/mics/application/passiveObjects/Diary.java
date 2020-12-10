package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBusImpl;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private static class DiaryHolder{
        private static Diary instance = new Diary();
    }


    private int totalAttack ;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private Diary()
    {

        totalAttack = 0;
        HanSoloFinish = 0;
        C3POFinish = 0;
        R2D2Deactivate = 0;
        LeiaTerminate = 0;
        HanSoloTerminate = 0;
        C3POTerminate = 0;
        R2D2Terminate = 0;
        LandoTerminate = 0;
    }

    public static Diary getInstance(){
        return DiaryHolder.instance;
    }



    public void setHC3POFinish() {
        this.C3POFinish = System.currentTimeMillis();
    }

    public void setR2D2Deactivate() {
        this.R2D2Deactivate = System.currentTimeMillis();
    }

    public void setLeiaTerminate() {
        this.LeiaTerminate = System.currentTimeMillis();
    }

    public void setHanSoloTerminate() {
        this.HanSoloTerminate = System.currentTimeMillis();
    }

    public void setC3POTerminate() {
        this.C3POTerminate = System.currentTimeMillis();
    }

    public void setR2D2Terminate() {
        this.R2D2Terminate = System.currentTimeMillis();
    }

    public void setLandoTerminate() {
        this.LandoTerminate = System.currentTimeMillis();
    }
}

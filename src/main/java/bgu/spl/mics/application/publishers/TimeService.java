package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.DieUallMotherfuckersBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private int duration;
    private int counter;
    private int interval;


    public TimeService(int duration) {
        super("TimeService");
        this.duration = duration;
        interval = 100;
        counter = 1;
    }

    @Override
    protected void initialize() {
        run();
    }

    @Override
    public void run() {

        try {
            while (duration > counter) {
                TickBroadcast b = new TickBroadcast(counter);
                System.out.println(counter);
                getSimplePublisher().sendBroadcast(b);
                counter++;
                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("TS: sent DieUallMotherfuckersBroadcast");
		getSimplePublisher().sendBroadcast(new DieUallMotherfuckersBroadcast());
    }
}

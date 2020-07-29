package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.messages.releaseAgentsEvent;
import bgu.spl.mics.application.passiveObjects.Squad;
import javafx.util.Pair;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private Squad squad;
    private final int serialNumber;

    public Moneypenny(int SerialNumber) {
        super("moneyPenny");
        squad = Squad.getInstance();
        serialNumber = SerialNumber;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(AgentsAvailableEvent.class, (event) -> {
            List<String> a = event.getAgentsSerials();
            boolean isGood = squad.getAgents(a);
            int result = isGood ? serialNumber : -1;
            List<String> agentsNames = squad.getAgentsNames(a);
            Pair<Integer, List<String>> pairToSend = new Pair<>(result, agentsNames);
            complete(event, pairToSend);
            if(isGood) {
				int timeOfMission = event.getHandHolder().get();//stuck me until M decide
				if (timeOfMission == -1)
					squad.releaseAgents(event.getAgentsSerials());
				else
					squad.sendAgents(event.getAgentsSerials(),timeOfMission);
			}
        });

        subscribeEvent(SendAgentsEvent.class, (event) -> {
            squad.sendAgents(event.getSerials(), event.getTime());
            complete(event, 1);
        });

        subscribeEvent(releaseAgentsEvent.class, (event) -> {
            List<String> agentsToRelease = event.getAgents();
            squad.releaseAgents(agentsToRelease);
            complete(event, null);
        });

    }
}

package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;
import javafx.util.Pair;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class M extends Subscriber {
    private int currentTime;
    private int serialNumber;

    public M(int serialNumber) {
        super("M" + serialNumber);
        this.serialNumber = serialNumber;
        currentTime = 0;
    }

    @Override
    protected void initialize() {

        subscribeBroadcast(TickBroadcast.class, (b) -> {
            currentTime = b.getTick();
        });
        this.subscribeEvent(MissionReceivedEvent.class, (event) -> {

            List<String> agents = event.getInfo().getSerialAgentsNumbers();
            Diary diary = Diary.getInstance();
            diary.incrementTotal();

            // creating agentAvailableEvent and send it to the MB
            Future<Integer> handHolder = new Future<>();
            AgentsAvailableEvent agentsEventToSend = new AgentsAvailableEvent(agents, handHolder);
            Future<Pair<Integer, List<String>>> futureOfAgentsEvent = getSimplePublisher().sendEvent(agentsEventToSend);
            Pair<Integer, List<String>> moneyPenny = futureOfAgentsEvent.get();
            int moneyPennyNumber = moneyPenny.getKey();
            List<String> agentsNames = moneyPenny.getValue();
            GadgetAvailableEvent gadgetEventToSent;

            if (moneyPennyNumber != -1) {
                // creating gadgetAvailableEvent and send it to the MB
                gadgetEventToSent = new GadgetAvailableEvent(event.getInfo().getGadget());
                Future<Integer> futureOfGadgetEvent = getSimplePublisher().sendEvent(gadgetEventToSent);
                if (futureOfGadgetEvent != null) {
                    int qTime = futureOfGadgetEvent.get();
                    releaseAgentsEvent releaseAgents;
                    if (qTime > 0) {
                        // if the mission in time
                        int duration = event.getInfo().getDuration();
                        if (currentTime < event.getInfo().getTimeExpired()) {
                            handHolder.resolve(duration);
                            Report report = fillReport(event.getInfo(), agentsNames, moneyPennyNumber, qTime);
                            diary.addReport(report);
                            return;
                        }
                    }
                }
            }
            handHolder.resolve(-1);
        });
    }

    private Report fillReport(MissionInfo missionInfo, List<String> agentsNames, int moneyPenny, int qTime) {
        Report report = new Report();
        List<String> agentsNumbers = missionInfo.getSerialAgentsNumbers();
        report.setM(serialNumber);
        report.setMoneypenny(moneyPenny);
        report.setAgentsSerialNumbersNumber(agentsNumbers);
        report.setAgentsNames(agentsNames);
        report.setGadgetName(missionInfo.getGadget());
        report.setTimeIssued(missionInfo.getTimeIssued());
        report.setQTime(qTime);
        report.setTimeCreated(currentTime);

        return report;
    }
}

package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import javafx.util.Pair;

import java.util.List;

public class AgentsAvailableEvent implements Event<Pair<Integer, List<String>>> {
    List<String> agentsSerials;
    Future<Integer> handHolder;

    public AgentsAvailableEvent(List<String> agentsSerials, Future<Integer> handHolder) {
        this.agentsSerials = agentsSerials;
        this.handHolder = handHolder;
    }

    public List<String> getAgentsSerials() {
        return agentsSerials;
    }

    public Future<Integer> getHandHolder() {
        return handHolder;
    }
}

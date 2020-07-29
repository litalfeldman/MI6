package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class releaseAgentsEvent implements Event<Void>  {
    private List<String> serialAgentsNumbers;


    public releaseAgentsEvent (List<String> agents) {
        this.serialAgentsNumbers = agents;
    }

    public List<String> getAgents() {
        return serialAgentsNumbers;
    }
}

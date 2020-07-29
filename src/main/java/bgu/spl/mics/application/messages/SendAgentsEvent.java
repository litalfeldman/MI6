package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event<Integer> {
    private List<String> serials;
    private int time;

    public SendAgentsEvent(List<String> serials, int time) {
        this.time = time;
        this.serials = serials;
    }

    public int getTime() {
        return time;
    }

    public List<String> getSerials() {
        return serials;
    }
}

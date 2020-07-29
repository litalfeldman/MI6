package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    Squad S=null;
    Agent[] A;
    @BeforeEach
    public void setUp(){

    }

    @Test
    public void getAgentTest(){
        List<String> Alist = new LinkedList<>();
        Alist.add("005");
        Alist.add("006");
        Alist.add("007");
        assertFalse(S.getAgents(Alist));
        Alist.remove(1);
        assertTrue(S.getAgents(Alist));
        Alist.add("008");
        assertFalse(S.getAgents(Alist));
    }

    @Test
    public void releaseAgentsTest(){
        List<String> Alist = new LinkedList<>();
        Alist.add("006");
        assertFalse(A[1].getAvailable());
        S.releaseAgents(Alist);
        assertTrue(A[1].getAvailable());
    }

    @Test
    public void getAgentsNames(){
        List<String> Alist = new LinkedList<>();
        Alist.add("005");
        Alist.add("006");
        Alist.add("008");
        List<String> outputList = S.getAgentsNames(Alist);
        assertNull(outputList.get(2));
        Alist.remove(2);
        assertLinesMatch(Arrays.asList(new String[]{ "Stuart Thomas","Alec Trevelyan" }), outputList);
    }

    @Test
    public void sendAgents(){
        List<String> Alist = new LinkedList<>();
        Alist.add("005");
        Alist.add("007");
        S.sendAgents(Alist, 200);
        assertFalse(A[0].getAvailable());
    }

    @Test
    public void loadTest() {
        List<String> Alist = new LinkedList<>();
        Alist.add("005");
        Alist.add("007");
        Squad agents = Squad.getInstance();
        assertFalse(agents.getAgents(Alist));
        Agent[] toLoad = {new Agent("005", "Stuart Thomas", true), new Agent("007", "James Bond", true)};
        agents.load(toLoad);
        Alist.add("005");
        Alist.add("007");
        assertTrue(agents.getAgents(Alist));
    }
}
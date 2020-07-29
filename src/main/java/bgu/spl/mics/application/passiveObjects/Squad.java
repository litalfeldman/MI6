package bgu.spl.mics.application.passiveObjects;

import java.util.*;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

    private Map<String, Agent> Agents;

    private static class SingletonHolder {
        private static Squad instance = new Squad();
    }

    private Squad() {
        Agents=new HashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        return SingletonHolder.instance;
    }


    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        String serialNum;
        for (Agent agent : agents) {
            serialNum = agent.getSerialNumber();
            Agents.put(serialNum, agent);
        }
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
        Agent agentToUpdate;
        for (String serial : serials) {
            agentToUpdate = Agents.get(serial);
            agentToUpdate.release();
            synchronized (agentToUpdate) {
                agentToUpdate.notifyAll();
            }
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     */
    public void sendAgents(List<String> serials, int time) {

        try {
            Thread.currentThread().sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        releaseAgents(serials);
    }


    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public boolean getAgents(List<String> serials) {
        Agent agent;
        for (String serial : serials) {
            if (!Agents.containsKey(serial))
                return false;
        }

        serials.sort(Comparator.naturalOrder());
        try {
            for (String serial : serials) {
                agent = Agents.get(serial);
                //if two threads want the same agent, only one thread acquired the agent
                synchronized (agent) {
                    while (!agent.getAvailable()) {
                        agent.wait();
                    }
                    agent.acquire();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return true;
    }


    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
        List<String> namesList = new LinkedList<>();
        Agent agent;
        for (String serial : serials) {
            agent = Agents.get(serial);
            namesList.add(agent.getName());
        }
        return namesList;
    }
}
